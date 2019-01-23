import React, { Component } from 'react';
import { Table, Button, Loading } from '@icedesign/base';
import IceContainer from '@icedesign/container';
import '../../../../css/common.css';
import {httpUrl} from '../../../../common';

export default class TrackTable extends Component {

    constructor(props) {
        super(props);
        this.state = {
            loadingVisible: true,
            current: 1,
            total: 1,
            dataSource: [],
            tableName: [],
            defaultSelectValue: '0',
            searchValue: ''
        };
    }

    componentDidMount () {
      
        // 获取参数
        console.log(this.props)
        let groupId = this.props.groupId;
        let unitId = this.props.unitId;
        this.queryFileInfoList(groupId, unitId)
    }

    queryFileInfoList (groupId, unitId) {
        let that = this;
        httpUrl('GET', host + 'log/transaction-info?groupId=' + groupId + '&unitId=' + unitId, '','', res => {
            var list = [res];
            for (var i = 0; i < list.length; i++) {
                list[i].key = i + 1;
            }
            that.setState({
                dataSource : list,
                loadingVisible: false
            })
            if (pageNow == 1) {
                that.setState({
                    total : res.total
                })
            }
        }, res => {
            if(res == 'error') {
                this.props.path.history.push("/login");
            }
        })
    }
    
    getVal (value) {
        this.setState({
            searchValue: value
        })
    }

    onSelect (value) {
        this.setState({
            defaultSelectValue: value
        })
    }

    handlePaginationChange = (current) => {
        this.setState({
            current: current,
            loadingVisible: true
        });
        this.queryFileInfoList(current)
    };

    affirm () {
        let that = this;
        this.setState({
            loadingVisible: true
        });
        that.queryFileInfoList(1, this.state.searchValue, this.state.tableName[this.state.defaultSelectValue])
    }

    back () {
      history.go(-1)
    }
    render() {
        const dataSource = this.state.dataSource;

        return (
            <div>
                <div className="filter-table">
                    <Loading style={{zIndex: 100,position: 'fixed',top: '30%',left: '50%'}} shape="dot-circle" color="#444444" visible={this.state.loadingVisible}/>
                    <div className="Y-background-color-white" style={{borderRadius:'6px',padding: '10px 0',marginBottom: '20px'}}>
                      <Button
                      onClick={this.props.onSubmit}
                      type="primary"
                      style={{ marginLeft: '10px'}}
                      onClick={this.back.bind(this)}>
                      返回
                      </Button>
                    </div>
                    {/* <IceContainer title="内容筛选">
                        <IceFormBinderWrapper value={this.props.value}>
                            <div>
                                <Row wrap>
                                    <Col xxs={24} xs={24} l={24} style={styles.filterCol}>
                                        <label style={styles.filterTitle}>名称</label>
                                        <IceFormBinder>
                                            <Input name="app" onChange={this.getVal.bind(this)} value={this.state.searchValue}/>
                                        </IceFormBinder>
                                        <label style={styles.filterTitle}>表名</label>
                                        <Select onChange={this.onSelect.bind(this)} style={{width:'12%'}} value={this.state.defaultSelectValue}>
                                            {this.state.tableName.map((item, index) => {
                                                return (
                                                    <Option value={index}>{item}</Option>
                                                );
                                            })}
                                        </Select>
                                        <Button
                                            onClick={this.props.onSubmit}
                                            type="primary"
                                            style={{ marginLeft: '20px' }}
                                            onClick={this.affirm.bind(this)}>
                                            搜索
                                        </Button>
                                    </Col>
                                </Row>
                            </div>
                        </IceFormBinderWrapper>
                    </IceContainer> */}
                </div>
                <IceContainer>
                    <div style={styles.title} className="Y-flexbox-horizontal">
                        <h4 className="Y-vertical-middle">异常详情列表</h4>
                    </div>
                    <Table
                    dataSource={dataSource}
                    hasBorder={false}
                    style={{ padding: '0 20px 20px' }}
                    >
                    <Table.Column title="事务执行器" dataIndex="targetClazz" />
                    <Table.Column title="方法" dataIndex="method" />
                    <Table.Column title="方法字符串" dataIndex="methodStr" />
                    <Table.Column title="参数值" dataIndex="argumentValues" />
                    <Table.Column title="参数类型" dataIndex="parameterTypes" />
                    </Table>
                </IceContainer>
            </div>
        );
    }
}

const styles = {
  container: {
    margin: '0 20px',
    padding: '0 0 20px',
  },
  title: {
    margin: '0',
    padding: '10px 20px',
    fonSize: '16px',
    textOverflow: 'ellipsis',
    overflow: 'hidden',
    whiteSpace: 'nowrap',
    color: 'rgba(0,0,0,.85)',
    fontWeight: '500',
    borderBottom: '1px solid #ededed',
  },
  link: {
    margin: '0 5px',
    color: 'rgba(49, 128, 253, 0.65)',
    cursor: 'pointer',
    textDecoration: 'none',
  },
  separator: {
    margin: '0 8px',
    display: 'inline-block',
    height: '12px',
    width: '1px',
    verticalAlign: 'middle',
    background: '#e8e8e8',
  },
  pagination: {
    textAlign: 'right',
  },
  filterCol: {
    display: 'flex',
    alignItems: 'center',
    marginBottom: '20px',
  },

  filterTitle: {
    width: '68px',
    textAlign: 'right',
    marginRight: '12px',
    fontSize: '14px',
  },
};
