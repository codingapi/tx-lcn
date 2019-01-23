import React, { Component } from 'react';
import IceContainer from '@icedesign/container';
import CompositeFilter from '../CompositeFilter';
import './BuilderTable.css';


export default class BuilderTable extends Component {
  static displayName = 'BuilderTable';

  static propTypes = {};

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {};
  }

  renderState = (value) => {
    return (
      <div style={styles.state}>
        <span style={styles.circle} />
        <span style={styles.stateText}>{value}</span>
      </div>
    );
  };

  renderOper = () => {
    return (
      <div style={styles.oper}>
        <a href="/">查看</a>
      </div>
    );
  };

  columnsConfig = () => {
    return [
      {
        title: '内容',
        dataIndex: 'builder',
        key: 'builder',
      },
      {
        title: '时间',
        dataIndex: 'description',
        key: 'description',
      },
      {
        title: '详情',
        dataIndex: 'detail',
        key: 'detail',
        cell: this.renderOper,
      },
    ];
  };
  
  render() {
    return (
      <IceContainer>
        <div style={styles.tableHead}>
          <div style={styles.tableTitle}>资源清单</div>
        </div>
        <CompositeFilter />
        

      </IceContainer>
    );
  }
}

const styles = {
  tableHead: {
    margin: '0 0 20px',
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  tableTitle: {
    height: '20px',
    lineHeight: '20px',
    color: '#333',
    fontSize: '18px',
    fontWeight: 'bold',
    paddingLeft: '12px',
    borderLeft: '4px solid #666',
  },
  circle: {
    display: 'inline-block',
    background: '#28a745',
    width: '8px',
    height: '8px',
    borderRadius: '50px',
    marginRight: '4px',
  },
  stateText: {
    color: '#28a745',
  },
};
