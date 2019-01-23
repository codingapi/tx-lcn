import React, { Component } from 'react';
import { Table, Pagination, Button, Dialog, Input, Select, Loading, DatePicker, Feedback } from '@icedesign/base';
import IceContainer from '@icedesign/container';
import '../../../../css/common.css';
import {httpUrl, pageSize, isNull} from '../../../../common';
import {
FormBinderWrapper as IceFormBinderWrapper,
FormBinder as IceFormBinder,
} from '@icedesign/form-binder';
export default class TrackTable extends Component {
    static displayName = 'TrackTable';

    static propTypes = {};

    static defaultProps = {};

    constructor(props) {
        super(props);
        this.state = {
            loadingVisible: true,
            current: 1,
            total: 1,
            dataSource: [],
            tableName: [],
            searchValue: '',
            searchValue1: '',
            ld: '',
            rd: '',
            startValue: null,
            endValue: null,
            endOpen: false
        };
    }

    componentDidMount () {
        this.queryFileInfoList()
    }

    queryFileInfoList (pageNow, groupId, tag, ld, rd,timeOrder) {
        let that = this;
        let url = "";
        if (isNull(pageNow)) {
            pageNow = 1;
        }

        
        if (!isNull(groupId)) {
            url += '&groupId=' + groupId;
        }

        if (!isNull(tag)) {
            url += '&tag=' + tag;
        }
        
        if (!isNull(ld)) {
            url += '&ld=' + ld;
        }

        if (!isNull(rd)) {
            url += '&rd=' + rd;
        }

        if (!isNull(timeOrder)) {
            url += '&timeOrder=' + timeOrder;
        }
        httpUrl('GET', host + 'logs?page=' + pageNow + '&limit=' + pageSize + url, '','', res => {
            var list = res.logs;
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
    
    // 时间选择
    disabledStartDate(startValue) {
        const { endValue } = this.state;
        if (!startValue || !endValue) {
          return false;
        }
        return startValue.valueOf() > endValue.valueOf();
    }

    disabledEndDate(endValue) {
        const { startValue } = this.state;
        if (!endValue || !startValue) {
            return false;
        }
        return endValue.valueOf() <= startValue.valueOf();
    }

    onChange(field, value) {
        this.setState({
            [field]: value
        });
    }

    onStartChange(value,str) {
        this.setState({
            ld: str
        })
        this.onChange("startValue", value);
    }

    onEndChange(value,str) {
        this.setState({
            rd: str
        })
        this.onChange("endValue", value);
    }

    handleStartOpenChange(open) {
        if (!open) {
            this.setState({ endOpen: true });
        }
    }

    handleEndOpenChange(open) {
        this.setState({ endOpen: open });
    }

    getVal (value) {
        this.setState({
            searchValue: value
        })
    }

    getVal1 (value) {
        this.setState({
            searchValue1: value
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
        that.queryFileInfoList(1, this.state.searchValue, this.state.searchValue1,this.state.ld,this.state.rd)
    }

    // 清楚
    clear () {
        var that = this;
        Dialog.confirm({
            content: "您确定清除当前检索条件日志吗？",
            onOk: (res) => {

                var data = {
                    "groupId": this.state.searchValue,
                    "tag": this.state.searchValue1,
                    "ld": this.state.ld,
                    "rd": this.state.rd,
                }
    
                httpUrl('DELETE', host + 'logs', data,'', res => {
                    Feedback.toast.success('清除成功');
                    setTimeout(function () {
                        that.setState({
                            loadingVisible : false
                        })
                        that.queryFileInfoList()
                    },500)
                }, res => {
                    if(res == 'error') {
                        that.props.path.history.push("/login");
                    }
                })
            }
        });
        
    }

    onSort (dataIndex, order) {
        let that = this;
        if(order == 'asc') {
            // 升序
            that.queryFileInfoList(that.state.current, this.state.searchValue, this.state.searchValue1,this.state.ld,this.state.rd, 1)
        } else {
            that.queryFileInfoList(that.state.current, this.state.searchValue, this.state.searchValue1,this.state.ld,this.state.rd, 2)
        }
    }

    render() {
        const dataSource = this.state.dataSource;
        const { startValue, endValue, endOpen } = this.state;
        return (
            <div>
                <div className="filter-table">
                    <Loading style={{zIndex: 100,position: 'fixed',top: '30%',left: '50%'}} shape="dot-circle" color="#444444" visible={this.state.loadingVisible}/>
                    <IceContainer title="内容筛选">
                        <IceFormBinderWrapper value={this.props.value}>
                            <div className="Y-flexbox-horizontal">
                                <div className="Y-vertical-middle Y-margin-horizontal-right-25">
                                    <label style={styles.filterTitle}>事务组ID</label>
                                </div>
                                <div style={{width:'15%'}} className="Y-margin-horizontal-right-25">
                                <Input name="app" onChange={this.getVal.bind(this)} value={this.state.searchValue}/>
                                </div>
                                <div className="Y-vertical-middle Y-margin-horizontal-right-25">
                                    <label style={styles.filterTitle}>TAG</label>
                                </div>
                                <div style={{width:'15%'}}>
                                <Input name="app1" onChange={this.getVal1.bind(this)} value={this.state.searchValue1}/>
                                </div>
                                <div className="Y-vertical-middle Y-margin-horizontal-right-25">
                                    <label style={styles.filterTitle}>时间范围</label>
                                </div>
                                <div style={{width:'24%'}} className="Y-flexbox-horizontal">
                                <DatePicker
                                disabledDate={this.disabledStartDate.bind(this)}
                                showTime
                                hasClear
                                value={startValue}
                                placeholder="Start"
                                onChange={this.onStartChange.bind(this)}
                                onOpenChange={this.handleStartOpenChange.bind(this)}
                                />
                                <DatePicker
                                hasClear
                                className="Y-margin-horizontal-left-25"
                                disabledDate={this.disabledEndDate.bind(this)}
                                showTime
                                value={endValue}
                                placeholder="End"
                                onChange={this.onEndChange.bind(this)}
                                open={endOpen}
                                onOpenChange={this.handleEndOpenChange.bind(this)}
                                />
                                </div>
                                <div>
                                    <Button
                                        type="primary"
                                        style={{ marginLeft: '20px' }}
                                        onClick={this.affirm.bind(this)}
                                        >
                                        搜索
                                    </Button>
                                </div>
                                <div>
                                    <Button
                                        type="primary"
                                        style={{ marginLeft: '20px' }}
                                        onClick={this.clear.bind(this)}
                                        >
                                        清除
                                    </Button>
                                </div>
                            </div>
                        </IceFormBinderWrapper>
                    </IceContainer>
                </div>
                <IceContainer>
                    <div style={styles.title} className="Y-flexbox-horizontal">
                        <h4 className="Y-vertical-middle">系统日志列表</h4>
                    </div>
                    <Table
                    dataSource={dataSource}
                    hasBorder={false}
                    style={{ padding: '0 20px 20px' }}
                    onSort={this.onSort.bind(this)}
                    >
                    <Table.Column title="事务组ID" dataIndex="groupId" />
                    <Table.Column title="事务单元ID" dataIndex="unitId" />
                    <Table.Column title="TAG" dataIndex="tag" />
                    <Table.Column title="日志内容" dataIndex="content" />
                    
                    <Table.Column title="创建时间" dataIndex="createTime" sortable/>
                    </Table>
                    <Pagination
                    current={this.state.current}
                    onChange={this.handlePaginationChange.bind(this)}
                    total={this.state.total} 
                    style={styles.pagination}/>
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
