import React, { Component } from 'react';
import { Table, Pagination, Button, Loading, Dialog, Select, Form, Field, Input, Feedback} from '@icedesign/base';
import IceContainer from '@icedesign/container';
import FoundationSymbol from 'foundation-symbol';
import { withRouter } from 'react-router-dom';
import '../../../../css/common.css';
import {httpUrl, pageSize, isNull} from '../../../../common';
const FormItem = Form.Item;
@withRouter
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
            visible: false,
            defaultSelectValue: '0',
            addData: [],
            selectedRowKeys: [],
            title: ''
        };
        this.field = new Field(this);
    }

    componentDidMount () {
        this.exceptionsList()
    }


    // 权限管理列表
    exceptionsList (pageNow) {
        let that = this;
        if (isNull(pageNow)) {
            pageNow = 1;
        }
       

        httpUrl('GET', host + 'app-mods?page=' + pageNow + '&limit=' + pageSize, '','', res => {
            console.log(res)
            var list = res.appMods;
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

    handlePaginationChange = (current) => {
        this.setState({
            current: current,
            loadingVisible: true
        });
        this.exceptionsList(current)
    };

    add () {
        let that = this;
        that.field.setValue('filePath', '');
        that.field.setValue('userType', '');
        this.setState({
            visible : true,
            defaultSelectValue: '0',
            title: '新增'
        })
    }

    edit () {
        let that = this;
        var addData = this.state.addData;
        if (isNull(addData)) {
            Feedback.toast.error('请选择要编辑的项');
            return;
        }
        that.setState({
            visible: true,
            title: '编辑',
            defaultSelectValue: addData.filePermission
        })
        that.field.setValue('filePath', addData.filePath);
        that.field.setValue('userType', addData.userType);
    }

    delete () {
        let that = this;
        var addData = this.state.addData;
        if (isNull(addData)) {
            Feedback.toast.error('请选择要删除的项');
            return;
        }
        Dialog.confirm({
            content: "您确定删除吗？",
            onOk: (res) => {

                var data = {
                    "id": addData.id
                }
    
                httpUrl('POST', host + 'admin/permission/delete', data,'', res => {
                    Feedback.toast.success('删除成功');
                    setTimeout(function () {
                        that.setState({
                            visible : false,
                            selectedRowKeys: []
                        })
                        that.queryPermissionList()
                    },500)
                }, res => {
                    if(res == 'error') {
                        this.props.path.history.push("/login");
                    }
                })
            }
        });
        

    }

    onSelect (value) {
        this.setState({
            defaultSelectValue: value
        })
    }


    handleSubmit () {
        var that = this;
        this.field.validate((errors, values) => {
          if (errors) {
            console.log('Errors in form!!!');
            return;
          }

          if(this.state.title == '新增') {
            var data = {
              "filePath": values.filePath,
              "userType": values.userType,
              "filePermission": that.state.defaultSelectValue
            }
            httpUrl('POST', host + 'admin/permission/add', data,'', res => {
                Feedback.toast.success('添加成功');
                setTimeout(function () {
                    that.setState({
                        visible: false,
                        selectedRowKeys: []
                    })
                    that.queryPermissionList()
                },500)
            }, res => {
                if(res == 'error') {
                    this.props.path.history.push("/login");
                }
            })
        } else {
            var addData = this.state.addData
            var data = {
                'id': addData.id, 
                "filePath": values.filePath,
                "userType": values.userType,
                "filePermission": that.state.defaultSelectValue
            }
            httpUrl('POST', host + 'admin/permission/update', data,'', res => {
                Feedback.toast.success('修改成功');
                setTimeout(function () {
                    that.setState({
                        visible: false,
                        selectedRowKeys: []
                    })
                    that.queryPermissionList()
                },500)
            }, res => {
                if(res == 'error') {
                    this.props.path.history.push("/login");
                }
            })
        }
        })
    }

    onClose () {
        this.setState({
            visible : false
        })
    }

    // 查看详情
    detail (record) {
        this.props.history.push("/MonitorDetail?groupId=" + record.groupId + '&unitId=' + record.unitId)
    }

   

    filePermission = (value, index, record) => {
        var html = '';
        if(record.registrar == '0') {
            html = <span>TxManager通知事务</span>
        } else if(record.registrar== '1'){
            html = <span>TxClient查询事务状态</span>
        } else if(record.registrar== '2'){
            html = <span>事务发起方通知事务组</span>
        } else if(record.registrar== '-1'){
            html = <span>未知</span>
        }
        return html
    }

    operation = (value, index, record) => {
        var html = '';
        if(record.exState == '1') {
            html = <span>-</span>
        } else {
            html = <span style={{color: '#3086d0',cursor: 'pointer'}} onClick={this.detail.bind(this,record)}>详情</span>
        }
        return html
    }

    exState = (value, index, record) => {
        var html = '';
        if(record.exState == '0') {
            html = <span> <FoundationSymbol size="small" type='cross' /></span>
        } else {
            html = <span> <FoundationSymbol size="small" type='correct' /></span>
        }
        return html
    }

    // 返回
    back () {
        history.go(-1)
    }

    handleRowSelection (ids, records) {
        this.setState({
            selectedRowKeys: ids
        })
    }

    handleRowSelect (selected, record, records) {
        this.setState({
            addData: record
        })
    }

    handleRowSelectAll (selected, record, records) {
        
    }

    render() {
        const rowSelection = {
            onChange: this.handleRowSelection.bind(this),
            onSelect: this.handleRowSelect.bind(this),
            onSelectAll: this.handleRowSelectAll.bind(this),
            mode: 'single',
            selectedRowKeys: this.state.selectedRowKeys
        };
        const dataSource = this.state.dataSource;
        const init = this.field.init;
        const formItemLayout = {
        labelCol: {
            fixedSpan: 6,
        },
        wrapperCol: {
            span: 14,
        },
        };
        return (
            <IceContainer>
                <Loading style={{zIndex: 100,position: 'fixed',top: '30%',left: '50%'}} shape="dot-circle" color="#444444" visible={this.state.loadingVisible}/>
                <div>
                    <Button
                    type="primary"
                    style={{ marginLeft: '10px'}}
                    onClick={this.back.bind(this)}>
                    返回
                    </Button>
                </div>
                <div style={styles.title} className="Y-flexbox-horizontal">
                    <h4 className="Y-vertical-middle">在线模块列表</h4>
                    {/* <div className="Y-flex-item Y-text-align-right">
                        <Button
                        type="primary"
                        style={{ marginLeft: '10px' }}
                        onClick={this.add.bind(this)}>
                        新增
                        </Button>
                        <Button
                        type="primary"
                        style={{ marginLeft: '10px' }}
                        onClick={this.edit.bind(this)}>
                        编辑
                        </Button>
                        <Button
                        type="primary"
                        style={{ marginLeft: '10px' }}
                        onClick={this.delete.bind(this)}>
                        删除
                        </Button>
                    </div> */}
                    <Dialog
                    style={{ width: 600}}
                    visible={this.state.visible}
                    onOk={this.handleSubmit.bind(this)}
                    onCancel={this.onClose.bind(this)}
                    onClose={this.onClose.bind(this)}
                    title={this.state.title}
                    >
                    <div>
                        <Form direction="ver" field={this.field}>
                            <FormItem label="用户类型：" {...formItemLayout}>
                            <Input hasClear
                                {...init('userType', {
                                rules: [{ required: true, message: '必填选项' }],
                                })}
                            />
                            </FormItem>
                            <FormItem label="文件权限：" {...formItemLayout}>
                            <Select onChange={this.onSelect.bind(this)} style={{width:'40%'}} value={this.state.defaultSelectValue}>
                                <Option  value='0'>读</Option>
                                <Option  value='1'>写</Option>
                            </Select>
                            </FormItem>
                            <FormItem label="文件目录：" {...formItemLayout}>
                            <Input hasClear
                                {...init('filePath', {
                                rules: [{ required: true, message: '必填选项' }],
                                })}
                            />
                            </FormItem>
                        </Form>
                        </div>
                    </Dialog>
                </div>
                
                <Table
                dataSource={dataSource}
                hasBorder={false}
                style={{ padding: '0 20px 20px' }}
                
                >
                <Table.Column title="模块标识" dataIndex="modId" />
                <Table.Column title="注册时间" dataIndex="registerTime" />
                </Table>
                <Pagination
                current={this.state.current}
                onChange={this.handlePaginationChange.bind(this)}
                total={this.state.total} 
                style={styles.pagination}/>
            </IceContainer>
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
};
