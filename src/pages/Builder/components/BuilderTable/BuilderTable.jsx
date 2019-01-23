import React, { Component } from 'react';
import IceContainer from '@icedesign/container';
import { Button, Table, Pagination } from '@icedesign/base';
import CustomTable from '../../../../components/CustomTable';
import TableFilter from '../TableFilter';
import './BuilderTable.css';
const ButtonGroup = Button.Group;

const getData = () => {
  return Array.from({ length: 20 }).map((item, index) => {
    return {
      id: index + 1,
      builder: `监视器的阈值为队列失败操作监视器的阈值为队列失败操作监视器的阈值为队列失败操作监视器的阈值为队列失败操作`,
      name: '张三峰',
      createTime: `2018-06-${index + 1}`,
      state: '查看',
    };
  });
};

const getRowClassName = function(record) {
  console.log(record)
  if (record.id == 1 || record.id == 5 || record.id == 13) {
    return "highlight-row";
  }

  if (record.id == 4 || record.id == 8) {
    return "highlight-row-yellow";
  }
}
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
          <div style={styles.tableTitle}>活动警报</div>
        </div>
        <Table
          dataSource={getData()}
          hasBorder={false}
          getRowClassName={getRowClassName}
          style={{ padding: '0 20px 20px' }}
        >
          <Table.Column title="内容" dataIndex="builder" />
          <Table.Column title="时间" dataIndex="createTime" width={400}/>
          <Table.Column title="操作" dataIndex="state" width={200}/>
        </Table>

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
