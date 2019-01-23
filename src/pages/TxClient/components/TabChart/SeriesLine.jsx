import React, { Component } from 'react';
import { Chart, Axis, Geom, Tooltip } from 'bizcharts';
import { DataSet } from '@antv/data-set';

export default class SeriesLine extends Component {
  static displayName = 'SeriesLine';

  static propTypes = {};

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    // 参考：https://alibaba.github.io/BizCharts/
    // 数据源
    const data = [
      { month: 'Jan', '读取请求': 7.0, '持久连接': 20 ,'发送响应内容': 4, "关闭连接": 7.0 , "等待连接": 5.0 },
      { month: 'Feb', '读取数据': 6.9, '持久连接': 22 ,'发送响应内容': 20, "关闭连接": 7.0 , "等待连接": 7.0 },
      { month: 'Mar', '读取数据': 9.5, '持久连接': 24 ,'发送响应内容': 34, "关闭连接": 7.0 , "等待连接": 37.0 },
      { month: 'Apr', '读取数据': 14.5, '持久连接': 30 ,'发送响应内容': 46, "关闭连接": 7.0, "等待连接": 3.0  },
      { month: 'May', '读取数据': 18.4, '持久连接': 50 ,'发送响应内容': 26, "关闭连接": 7.0, "等待连接": 69.0  },
      { month: 'Jun', '读取数据': 21.5, '持久连接': 65,'发送响应内容': 12 , "关闭连接": 7.0 , "等待连接": 13.0 },
      { month: 'Jul', '读取数据': 25.2, '持久连接': 70,'发送响应内容': 79 , "关闭连接": 7.0 , "等待连接": 25.0 },
      { month: 'Aug', '读取数据': 26.5, '持久连接': 80,'发送响应内容': 46 , "关闭连接": 7.0 , "等待连接": 47.0 },
      { month: 'Sep', '读取数据': 23.3, '持久连接': 85 ,'发送响应内容': 20, "关闭连接": 7.0, "等待连接": 14.0  },
      { month: 'Oct', '读取数据': 18.3, '持久连接': 90 ,'发送响应内容': 25, "关闭连接": 7.0, "等待连接": 7.0  },
      { month: 'Nov', '读取数据': 13.9, '持久连接': 80 ,'发送响应内容': 20, "关闭连接": 7.0 , "等待连接": 9.0 },
      { month: 'Dec', '读取数据': 9.6, '持久连接': 70 ,'发送响应内容': 11, "关闭连接": 7.0, "等待连接": 7.0  },
    ];

    // DataSet https://github.com/alibaba/BizCharts/blob/master/doc/tutorial/dataset.md#dataset
    const ds = new DataSet();
    const dv = ds.createView().source(data);
    dv.transform({
      type: 'fold',
      fields: ['读取数据', '持久连接', '发送响应内容', '关闭连接', '等待连接'],
      key: 'city',
      value: 'temperature',
    });

    // 定义度量
    const cols = {
      month: {
        range: [0, 1],
      },
    };

    return (
      <div className="chart-line">
        <Chart
          height={300}
          data={dv}
          scale={cols}
          forceFit
          padding={[40, 35, 40, 35]}
        >
          <Axis name="month" />
          <Axis name="temperature" label={{ formatter: (val) => `${val}` }} />
          <Tooltip crosshairs={{ type: 'y' }} />
          <Geom
            type="line"
            position="month*temperature"
            size={2}
            color="city"
            shape="smooth"
          />
          <Geom
            type="point"
            position="month*temperature"
            size={4}
            shape="circle"
            color="city"
            style={styles.point}
          />
        </Chart>
      </div>
    );
  }
}

const styles = {
  point: {
    stroke: '#fff',
    lineWidth: 1,
  },
};
