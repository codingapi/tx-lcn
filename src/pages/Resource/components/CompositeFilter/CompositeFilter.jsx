import React, { Component } from 'react';
import { Search, Tab, DatePicker, Table, Pagination } from '@icedesign/base';
import IceContainer from '@icedesign/container';
import { enquireScreen } from 'enquire-js';

const TabPane = Tab.TabPane;

export default class CompositeFilter extends Component {
  static displayName = 'CompositeFilter';

  static propTypes = {};

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {
      isMobile: false,
      dataSource: []
    };
  }

  componentDidMount() {
    this.enquireScreenRegister();
  }

  enquireScreenRegister = () => {
    const mediaCondition = 'only screen and (max-width: 720px)';

    enquireScreen((mobile) => {
      this.setState({
        isMobile: mobile,
      });
    }, mediaCondition);
  };

  onTabChange = (key) => {
    console.log(key)
    this.setState({
      key: key
    });
    if(key == 'video') {
      const getData = () => {
        return Array.from({ length: 20 }).map((item, index) => {
          return {
            id: index + 1,
            builder: `交换机 机架式 交换机24网络交换机百兆`,
            createTime: '192.168.1.109',
            state: 'TP-LINK TL-SF1024S',
          };
        });
      };
      this.setState({
        dataSource: getData()
      });
    } else if(key == 'pic') {
      const getData = () => {
        return Array.from({ length: 20 }).map((item, index) => {
          return {
            id: index + 1,
            builder: `OPM-ESJSLAJD-ZOHKNAS.JJDIOS.COM`,
            createTime: '192.168.1.109',
            state: 'Windows 8',
          };
        });
      };
      this.setState({
        dataSource: getData()
      });
    } else if (key == 'storage') {
      const getData = () => {
        return Array.from({ length: 20 }).map((item, index) => {
          return {
            id: index + 1,
            builder: `aggr0`,
            createTime: '855',
            state: '814.2',
          };
        });
      };
      this.setState({
        dataSource: getData()
      });
    } else {
      this.setState({
        dataSource: []
      });
    }
  };

  onTagChange = (key, selected) => {
    console.log(`Tag: ${key} is ${selected ? 'selected' : 'unselected'}`);
  };

  onDateChange = (value) => {
    console.log(value);
  };

  onSearch = (value) => {
    console.log(value);
  }

  render() {
    console.log(this.state.key)
    return (
      <div className="composite-filter">
        <IceContainer style={styles.filterCard}>
          <Tab
            type="text"
            onChange={this.onTabChange}
            contentStyle={{ display: 'none' }}
            tabBarExtraContent={
              !this.state.isMobile ? null : null
            }
          >
            <TabPane tab="Virtual Servers" key="all" />
            <TabPane tab="VMware" key="pic" />
            <TabPane tab="Gyper-v" key="item" />
            <TabPane tab="Xen" key="new" />
            <TabPane tab="设备" key="video" />
            <TabPane tab="存储" key="storage" />
          </Tab>
          {this.state.key == 'storage' ? 
             <Table
                  dataSource={this.state.dataSource}
                  hasBorder={false}
                  style={{ padding: '0 20px 20px' }}
                >
                  <Table.Column title="磁盘名称" dataIndex="builder" />
                  <Table.Column title="总容量" dataIndex="createTime" width={400}/>
                  <Table.Column title="使用容量" dataIndex="state" width={200}/>
                </Table>
              :
              <Table
              dataSource={this.state.dataSource}
              hasBorder={false}
              style={{ padding: '0 20px 20px' }}
            >
              <Table.Column title="标题" dataIndex="builder" />
              <Table.Column title="IP地址" dataIndex="createTime" width={400}/>
              {this.state.key == 'video' ? 
              <Table.Column title="型号" dataIndex="state" width={200}/>:
              <Table.Column title="系统" dataIndex="state" width={200}/>
              }
            </Table>
          }
        </IceContainer>
      </div>
    );
  }
}

const styles = {
  filterCard: {
    position: 'relative',
    padding: 10,
  },
  tagList: {
    marginTop: '10px',
  },
  extraFilter: {
    marginTop: '8px',
    display: 'flex',
    flexDirection: 'row',
  },
  search: {
    marginLeft: '12px',
  },
};
