import React, { Component } from 'react';

import IceContainer from '@icedesign/container';
import { Grid } from '@icedesign/base';
import PieChart from './PieChart';
import { Chart, Geom, Axis, Tooltip, Coord, Label, Legend } from 'bizcharts';
import { DataView } from '@antv/data-set';
const { Row, Col } = Grid;

export default class BaseInfo extends Component {
  static displayName = 'BaseInfo';

  static propTypes = {};

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {};
  }

  renderUserStatistics = () => {
    const data = [
      { type: '等待链接', count: 151, date: '02-21' },
      { type: '关闭连接', count: 532, date: '02-21' },
      { type: '持久连接', count: 1834, date: '02-21' },

      { type: '等待链接', count: 251, date: '02-22' },
      { type: '关闭连接', count: 732, date: '02-22' },
      { type: '持久连接', count: 2534, date: '02-22' },

      { type: '等待链接', count: 311, date: '02-23' },
      { type: '关闭连接', count: 932, date: '02-23' },
      { type: '持久连接', count: 1234, date: '02-23' },

      { type: '等待链接', count: 221, date: '02-24' },
      { type: '关闭连接', count: 632, date: '02-24' },
      { type: '持久连接', count: 2534, date: '02-24' },

      { type: '等待链接', count: 121, date: '02-25' },
      { type: '关闭连接', count: 532, date: '02-25' },
      { type: '持久连接', count: 2114, date: '02-25' },

      { type: '等待链接', count: 221, date: '02-26' },
      { type: '关闭连接', count: 632, date: '02-26' },
      { type: '持久连接', count: 2534, date: '02-26' },

      { type: '等待链接', count: 311, date: '02-27' },
      { type: '关闭连接', count: 932, date: '02-27' },
      { type: '持久连接', count: 1234, date: '02-27' },
    ];
    const dv = new DataView()
      .source(data)
      .transform({
        type: 'fill-rows',
        groupBy: ['type'],
        orderBy: ['date'],
      })
      .transform({
        type: 'impute',
        field: 'count',
        method: 'value',
        value: 0,
      });
    const cols = {
      date: {
        tickInterval: 10,
        nice: false,
      },
      count: {
        // nice: false,
        min: -500,
      },
    };
    return (
      <IceContainer title="请求信息">
        <div>
          <Chart
            height={300}
            data={dv}
            scale={cols}
            padding={[20, 0, 80, 50]}
            plotBackground={{ stroke: '#ccc' }}
            forceFit
          >
            <Axis name="date" line={null} />
            <Axis
              name="count"
              line={null}
              tickLine={{
                length: 8,
              }}
              subTickCount={10}
              subTickLine={{
                lineWidth: 1, // 子刻度线宽
                stroke: '#ddd', // 子刻度线颜色
                length: 5,
              }}
              grid={null}
            />
            <Legend
              position="bottom"
              useHtml
              g2-legend-marker={{
                borderRadius: 'none',
              }}
              g2-legend-title={{
                fontSize: '12px',
                fontWeight: 500,
                margin: 0,
                color: '#ff8800',
              }}
            />
            <Tooltip shared={false} crosshairs={false} inPlot={false} />
            <Geom
              type="area"
              position="date*count"
              color="type"
              adjust={['stack', 'symmetric']}
              shape="smooth"
              opacity={1}
            />
          </Chart>
        </div>
      </IceContainer>
    );
  };

  render() {
    return (
      <Row wrap gutter={20} style={styles.row}>
        <Col l="12">
          <IceContainer style={styles.container}>
            {/* <h4 style={styles.title}>应用版本详细信息</h4> */}
            {this.renderUserStatistics()}
          </IceContainer>
        </Col>
        <Col l="12">
          <IceContainer style={styles.container}>
            <h4 style={styles.title}>内存使用率</h4>
            <div style={{paddingBottom: '110px'}}>
            <PieChart />
            </div>
          </IceContainer>
        </Col>
      </Row>
    );
  }
}

const styles = {
  row: {
    margin: '20px 10px',
  },
  container: {
    margin: '0',
    padding: '0',
  },
  title: {
    margin: '0',
    padding: '15px 20px',
    fonSize: '16px',
    textOverflow: 'ellipsis',
    overflow: 'hidden',
    whiteSpace: 'nowrap',
    color: 'rgba(0,0,0,.85)',
    fontWeight: '500',
    borderBottom: '1px solid #eee',
  },
  summary: {
    padding: '20px',
  },
  item: {
    height: '32px',
    lineHeight: '32px',
  },
  label: {
    display: 'inline-block',
    fontWeight: '500',
    minWidth: '74px',
  },
};
