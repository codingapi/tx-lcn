/* eslint react/no-string-refs:0 */
import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { Input, Button, Checkbox, Grid, Feedback, } from '@icedesign/base';
import {httpUrl, isNull } from '../../../../common';
import {
    FormBinderWrapper as IceFormBinderWrapper,
    FormBinder as IceFormBinder,
    FormError as IceFormError,
  } from '@icedesign/form-binder';
import IceIcon from '@icedesign/icon';
import './UserLogin.scss';

const { Row, Col } = Grid;

// 寻找背景图片可以从 https://unsplash.com/ 寻找
const backgroundImage = 'https://img.alicdn.com/tfs/TB1zsNhXTtYBeNjy1XdXXXXyVXa-2252-1500.png';

@withRouter
export default class UserLogin extends Component {

    constructor(props) {
        super(props);
        this.state = {
            value: {
                account: undefined,
                pwd: undefined,
                checkbox: false,
                searchValue: ''
            },
            loginInfo: '',
            title: 'TxManager系统后台'
        };
        this.handleEnterKey = this.handleEnterKey.bind(this)
    }
    componentDidMount(){
            document.addEventListener("keydown",this.handleEnterKey);
    }
    formChange = (value) => {
        this.setState({
            value: value
        });
    };
    
    handleSubmit = (e) => {
        e.preventDefault();
        this.refs.form.validateAll((errors, values) => {
            if (errors) {
                return;
            }
            httpUrl('post', host + 'login?password=' + values.pwd, '','', res => {
                console.log(res)
                // 存储tokentoken
                sessionStorage.setItem("token", res.token);
                Feedback.toast.success('登录成功');
                this.props.history.push("/");
            });
        });
    };
    
    getVal (value) {
        this.setState({
            searchValue: value
        })
    }

    // 点击回车进行登录
    handleEnterKey (e) {
        if (e.keyCode === 13) {
            console.log(this.state.searchValue)
            if(isNull(this.state.searchValue)) {
                Feedback.toast.error('密码不能为空');
                return false;
            }
            httpUrl('post', host + 'login?password=' + this.state.searchValue, '','', res => {
                sessionStorage.setItem("token", res.token);
                Feedback.toast.success('登录成功');
                this.props.history.push("/");
            });
        }
    }

    
    render() {
        return (
        <div style={styles.userLogin} className="user-login">
            <div style={{...styles.userLoginBg, backgroundImage: `url(${backgroundImage})`}} />
            <div style={styles.contentWrapper} className="content-wrapper">
                <h2 style={styles.slogan} className="slogan">
                    欢迎使用<br /> {this.state.title}
                </h2>
                <div style={styles.formContainer}>
                    <h4 style={styles.formTitle}>登录</h4>
                    <IceFormBinderWrapper value={this.state.value} onChange={this.formChange} ref="form" >
                        <div style={styles.formItems}>

                            <Row style={styles.formItem}>
                                <Col>
                                    <IceIcon type="lock" size="small" style={styles.inputIcon} />
                                    <IceFormBinder name="pwd" required message="必填">
                                        <Input htmlType="password" placeholder="密码" id="password" onChange={this.getVal.bind(this)}/>
                                    </IceFormBinder>
                                </Col>
                                <Col>
                                    <IceFormError name="pwd" />
                                </Col>
                            </Row>

                            {/* <Row style={styles.formItem}>
                                <Col>
                                    <IceFormBinder name="checkbox">
                                        <Checkbox style={styles.checkbox}>记住账号</Checkbox>
                                    </IceFormBinder>
                                </Col>
                            </Row> */}

                            <Row style={styles.formItem}>
                                <Button type="primary" onClick={this.handleSubmit} style={styles.submitBtn}>
                                    登 录
                                </Button>
                            </Row>

                            {/* <Row className="tips" style={styles.tips}>
                                没有账号？
                                <a href="#register" style={styles.link}>
                                    去注册
                                </a>

                                <span style={styles.line}>|</span>
                                <a href="#forgetPwd" style={styles.link}>
                                    忘记密码
                                </a>
                            </Row> */}
                        </div>
                    </IceFormBinderWrapper>
                </div>
            </div>
        </div>
        );
    }
}

const styles = {
  userLogin: {
    position: 'relative',
    height: '100vh',
  },
  userLoginBg: {
    position: 'absolute',
    top: 0,
    left: 0,
    right: 0,
    bottom: 0,
    backgroundSize: 'cover',
  },
  formContainer: {
    display: 'flex',
    justifyContent: 'center',
    flexDirection: 'column',
    padding: '30px 40px',
    background: '#fff',
    borderRadius: '6px',
    boxShadow: '1px 1px 2px #eee',
  },
  formItem: {
    position: 'relative',
    marginBottom: '25px',
    flexDirection: 'column',
  },
  formTitle: {
    margin: '0 0 20px',
    textAlign: 'center',
    color: '#3080fe',
    letterSpacing: '12px',
  },
  inputIcon: {
    position: 'absolute',
    left: '0px',
    top: '3px',
    color: '#999',
  },
  submitBtn: {
    width: '240px',
    background: '#3080fe',
    borderRadius: '28px',
  },
  checkbox: {
    marginLeft: '5px',
  },
  tips: {
    textAlign: 'center',
  },
  link: {
    color: '#999',
    textDecoration: 'none',
    fontSize: '13px',
  },
  line: {
    color: '#dcd6d6',
    margin: '0 8px',
  },
};
