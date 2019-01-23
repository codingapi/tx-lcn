import React, { Component } from 'react';
import IceContainer from '@icedesign/container';
import { Chart, Geom, Axis, Tooltip, Coord, Label, Legend } from 'bizcharts';
import BuilderTable from './components/BuilderTable';

import PieChart from './PieChart';
export default class Builder extends Component {
  static displayName = 'Builder';

  constructor(props) {
    super(props);
    this.state = {};
  }

  renderTrafficTypes = () => {
    const data = [
      { status: '激活', count: 356 },
      { status: '保持', count: 235 },
      { status: '关闭', count: 245 },
    ];
    let total = 0;
    data.forEach((item) => {
      total += item.count;
    });
    const precentages = {};
    data.forEach((item) => {
      precentages[item.status] = item.count / total;
    });

    const cols = {
      count: {
        min: 0,
      },
    };
    return (
      <IceContainer title="资源清单统计">
        <div>
          <Chart
            height={300}
            data={data}
            scale={cols}
            padding={[70, 20, 100, 20]}
            forceFit
          >
            <Coord type="polar" />
            <Axis
              name="count"
              label={null}
              tickLine={null}
              line={{
                stroke: '#E9E9E9',
                lineDash: [3, 3],
              }}
            />
            <Axis
              name="status"
              grid={{
                align: 'center',
              }}
              tickLine={null}
              label={{
                Offset: 10,
                textStyle: {
                  textAlign: 'center', // 设置坐标轴 label 的文本对齐方向
                },
              }}
            />
            <Legend
              itemFormatter={(text) => {
                return `${text} (${(precentages[text] * 100).toFixed(2)}%)`;
              }}
              name="status"
              itemWidth={100}
            />
            <Tooltip />
            <Geom
              type="interval"
              position="status*count"
              color="status"
              style={{
                lineWidth: 1,
                stroke: '#fff',
              }}
            >
              <Label
                content="count"
                offset={-15}
                textStyle={{
                  textAlign: 'center',
                  fontWeight: 'bold',
                  fontSize: 11,
                }}
              />
            </Geom>
          </Chart>
        </div>
      </IceContainer>
    );
  };
  
  render() {
    return (
      <div>
        
        {this.renderTrafficTypes()}
        <div style={{marginTop: '20px'}}>
        <BuilderTable/>
        </div>
       
      </div>
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
