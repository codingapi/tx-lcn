import React, { Component } from 'react';
import IceContainer from '@icedesign/container';
import { Grid } from '@icedesign/base';

import SplineChart from '../SplineChart';

const { Row, Col } = Grid;

export default class BuilderState extends Component {
  static displayName = 'BuilderState';

  static propTypes = {};

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    const totalData = [
      {
        label: '正常',
        value: '55464',
      },
      {
        label: '警报',
        value: '5',
      },
      {
        label: '空闲',
        value: '4274',
      },
      {
        label: '繁忙',
        value: '689',
      },
    ];

    const todayData = [
      {
        label: '设备请求',
        value: '79905',
        img: require('./images/count.png'),
      },
      {
        label: '在线用户',
        value: '3',
        img: require('./images/repo.png'),
      },
      {
        label: '告警次数',
        value: '25',
        img: require('./images/user.png'),
      },
      {
        label: '巡检次数',
        value: '550',
        img: require('./images/builder.png'),
      },
    ];
    return (
      <Row gutter="20">
        <Col l="12">
          <IceContainer>
            <h4 style={styles.cardTitle}>最近一小时性能监控</h4>
            <SplineChart />
          </IceContainer>
        </Col>
        <Col l="12">
          <IceContainer>
            <h4 style={styles.cardTitle}>所有节点</h4>
            <Row wrap gutter="10">
              {totalData.map((item, index) => {
                return (
                  <Col key={index} style={{ background: 'red' }}>
                    <div style={styles.totalCard}>
                      <div style={styles.label}>{item.label}</div>
                      <div style={styles.value}>{item.value}</div>
                    </div>
                  </Col>
                );
              })}
            </Row>
          </IceContainer>
          <IceContainer>
            <h4 style={styles.cardTitle}>今日数据</h4>
            <Row wrap gutter="10">
              {todayData.map((item, index) => {
                return (
                  <Col key={index} style={{ background: 'red' }}>
                    <div style={styles.todayCard}>
                      <img src={item.img} alt="" style={styles.todayCardIcon} />
                      <div>
                        <div style={styles.label}>{item.label}</div>
                        <div style={styles.value}>{item.value}</div>
                      </div>
                    </div>
                  </Col>
                );
              })}
            </Row>
          </IceContainer>
        </Col>
      </Row>
    );
  }
}

const styles = {
  cardTitle: {
    margin: '0 0 20px',
    fontSize: '18px',
    paddingBottom: '15px',
    fontWeight: 'bold',
    borderBottom: '1px solid #eee',
  },
  totalCard: {
    maxWidth: '160px',
    padding: '10px',
    borderRadius: '4px',
    background: 'rgba(240,130,76,.8)',
    color: '#fff',
  },
  todayCard: {
    display: 'flex',
    alignItems: 'center',
  },
  todayCardIcon: {
    width: '36px',
    height: '36px',
    marginRight: '8px',
  },
  label: {
    height: '14px',
    lineHeight: '14px',
    marginBottom: '8px',
  },
  value: {
    height: '28px',
    lineHeight: '28px',
    fontSize: '28px',
    fontWeight: '500',
  },
};
