import React, { Component } from 'react';
import IceContainer from '@icedesign/container';
import { Form, Step, Grid, Input, Button,  Feedback,Loading} from '@icedesign/base';
import {
  FormBinderWrapper,
  FormBinder,
  FormError,
  FormBinder as IceFormBinder,
  FormError as IceFormError,
} from '@icedesign/form-binder';
import {httpUrl, isNull, phone,testPhone} from '../../../../common';
import '../../../../css/common.css';
//1.路由跳转页面引入
import { withRouter } from 'react-router'
const { Col } = Grid;

let codeTime = 60;
if(!isNull(sessionStorage.getItem('codeTime'))){
  codeTime = JSON.parse(sessionStorage.getItem('codeTime'))
}
let timer;
@withRouter//2.路由跳转引入
export default class forgetPwd extends Component {
  static displayName = 'forgetPwd';

  //页面参数默认值
  constructor(props) {
    super(props);
    this.state = {
        step: 0,//页面设置
        value: {
            account: '',//账号
            phone: '',
            code: ''
          },
        password:{
          account: '',//账号
          newPwd: '',//新密码
        },
        timerTitle:'获取验证码',

        timerCount:codeTime,
        disabled:true,
        loadingVisible: false,
    };
  }
  


  componentDidMount() {
    if(codeTime==60){
      this.setState({
        disabled:false
      })
    }
    this.interval(codeTime,timer)
  }

  interval(){
    if(codeTime!=60){
      this.interval=setInterval(() =>{
        timer=this.state.timerCount-1
        sessionStorage.setItem('codeTime',timer);
        // console.log(this.state.timerCount);
        this.setState({
          timerTitle:timer+'s重新获取',
          disabled: true,
          })
        if(timer<=0){
          this.interval&&clearInterval(this.interval);
          sessionStorage.setItem('codeTime',60);
          codeTime = JSON.parse(sessionStorage.getItem('codeTime'))
          this.setState({
            timerTitle: "获取验证码",
            timerCount: codeTime,
            disabled: false,
          })
        }else{
          this.setState({
          timerCount:timer,
          })
        }
        },1000
      )
    }
  }

  play(){
    var music = document.getElementById('music')
    music.play();
  }

  //第一个form的change方法
  formChange = value => {
    console.log('value', value);
    this.setState({
      value
    });
  };
//第二个form的change方法
  formChange2 = value => {
    console.log('value', value);
    this.setState({
      value
    });
  };

  handleSendPhoneNum = (e) => {
    e.preventDefault();
    let phone = this.state.value.phone
    if(isNull(phone)){
      Feedback.toast.error('手机号不能为空!');
      return;
    }
    if(!testPhone(phone)){
      Feedback.toast.error('手机号格式不对!');
            return;
      }
      this.setState({
        loadingVisible : true
      })
      var params = {'mobile':phone}
          httpUrl(host + 'pc/company/getRegisterMobile',params,'', res => {
              if(res.data.res.data == true){
                Feedback.toast.success('获取成功,请注意查收');
                this.setState({
                  loadingVisible : false
                })

                this.interval=setInterval(() =>{
                  timer=this.state.timerCount-1
                  // console.log(this.state.timerCount);
                 // console.log(timer+'s重新获取');
                 sessionStorage.setItem('codeTime',timer);
                 this.setState({
                   timerTitle:timer+'s重新获取',
                   disabled: true,
                   })
                   
                 if(timer===0){
                    this.interval&&clearInterval(this.interval);
                    sessionStorage.setItem('codeTime',60);
                    this.setState({
                      timerTitle: "获取验证码",
                      timerCount: codeTime,
                      disabled: false,
                    })
                 }else{
                    this.setState({
                      timerCount:timer,
                    })
                  }
                 },1000)
              }else{
                Feedback.toast.prompt('短信发送失败,一天不能超过5次');
                this.setState({
                  loadingVisible : false
                })
              }
          });
    }
  //提交并进入下一步
  nextStep = () => {
    this.refs.form.validateAll((error, value) => {
      // /mobile/pc/company/foreignPwd      
      if (!error || error.length === 0) {
        console.log(value)
        var params = {'account':value.account,'mobile':value.phone,'code':value.code}
          httpUrl(host + 'pc/company/foreignPwd',params,'', res => {
            console.log(res.data.res.data)
            if(res.data.res.data == true){
              this.state.password.account = value.account;
              this.setState({ step: this.state.step + 1 });
              sessionStorage.setItem('codeTime',60);
            }
          })
          
        }else{
          Feedback.toast.prompt('请补充完整表单信息');
      }
    });
  };

//提交并进入下一步
nextStep2 = () => {
  this.refs.form2.validateAll((error, value) => {
    if (!error || error.length === 0) {
        console.log(value)
        httpUrl(host + 'pc/company/updateNewPwd',value,'', res => {
          console.log(res.data.res.data)
          if(res.data.res.data == true){
            sessionStorage.setItem('codeTime',60);
            Feedback.toast.success('修改密码成功,跳转到登录页面');
            this.props.history.push("/login");
          }else{
            // Feedback.toast.prompt('修改密码成功,跳转到登录页面');
          }
        })
      }else{
        Feedback.toast.prompt('请补充完整表中信息');
    }
  });
};

renderStep = (step) => {
   
    if (step === 0) {
      return (
        <IceContainer style={styles.form}>
          <FormBinderWrapper
            ref="form"
            value={this.state.value}
            onChange={this.formChange}
            >
            <Col xxs="15" s="15" l="10" className="margin-auto">
                <div style={styles.formRow}>
                    <Col xxs="13" s="4" l="4" style={styles.formLabel} className="public-left">
                    </Col>
                    <Col  xxs="13" s="14" l="14" className="public-left">
                        <FormBinder required message="账号不能为空!" min={1}>
                            <Input placeholder="请输入您的账号" name="account" size="large" style={{ width: '100%',lineHeight: '40px'}} />
                        </FormBinder>
                        <div style={styles.formErrorWrapper}>
                            <FormError name="account" />
                        </div>
                    </Col>
                </div>

                <div style={styles.formRow}>
                    <Col  xxs="18" s="18" l="17" className="public-left">
                        <div style={{width: '160px',float: 'left',position: 'relative'}}>
                            <FormBinder required message="请输入正确的手机号" pattern = {phone} >
                                <Input placeholder="手机号" name="phone" size="large" style={{ width: '100%',lineHeight: '40px' }} />
                            </FormBinder>
                        </div>
                        <div style={{width: '112px', float: 'left'}}>
                                <Button type="primary" size="medium"
                                    onClick={this.handleSendPhoneNum.bind(this)} 
                                    style={styles.submitBtn}
                                    disabled={this.state.disabled}
                                > 
                                    {this.state.timerTitle}
                                </Button>
                                <Loading style={{position: 'absolute',paddingTop: '35px',paddingLeft: '50px',zIndex: 100}} shape="flower"  color="#444444" visible={this.state.loadingVisible}/>
                        </div>
                        {/* <div style={styles.formItemCol}> */}
                            
                        {/* </div> */}
                        <div style={{float:'right',width:'120px',marginTop:'6px',marginRight:'205px'}}>
                          <FormError name="phone" />
                        </div>
                    </Col>
                </div>
                    
                <div style={styles.formRow}>
                    <Col xxs="13" s="4" l="4" style={styles.formLabel} className="public-left">
                    </Col>
                    <Col  xxs="18" s="18" l="14" className="public-left">
                        <FormBinder required message="请输入正确的验证码">
                            <Input placeholder="请输入验证码" name="code" size="large" style={{ width: '100%',lineHeight: '40px' }} />
                        </FormBinder>
                        <div style={styles.formErrorWrapper}>
                            <FormError name="code" />
                        </div>
                    </Col>
                </div>
                <div style={{paddingLeft:'100px'}}>
                <Button onClick={this.nextStep} type="primary" style={{height: '38px',marginTop: '50px'}}>
                                下一步
                </Button>
                </div>
                </Col>
            </FormBinderWrapper>
        </IceContainer>
      );
    }else if (step === 1) {
      return (
        <IceContainer style={styles.form}>
          <FormBinderWrapper
            ref="form2"
            value={this.state.password}
            onChange={this.formChange2}
            >
            <Col xxs="15" s="15" l="10" className="margin-auto">
                <div style={styles.formRow}>
                    <Col xxs="13" s="4" l="4" style={styles.formLabel} className="public-left">
                    </Col>
                    <Col  xxs="13" s="14" l="14" className="public-left">
                        <FormBinder required message="请输入6-12位的密码!" min={6} max={12}>
                            <Input htmlType="password" placeholder="请输入新密码" name="newPwd" size="large" style={{ width: '100%',lineHeight: '40px'}} />
                        </FormBinder>
                        <div style={styles.formErrorWrapper}>
                            <FormError name="newPwd" />
                        </div>
                    </Col>
                </div>
                <div style={{paddingLeft:'100px'}}>
                <Button onClick={this.nextStep2} type="primary" style={{height: '38px',marginTop: '50px'}}>
                                下一步
                </Button>
                </div>
                </Col>
            </FormBinderWrapper>
        </IceContainer>
      );
    } 
  };

  render() {
      return (
          <div className="simple-fluency-form">
              <div style={styles.head}>
                  <Col xxs="24" s="24" l="12" className="margin-auto" style={styles.headText}>
                      找回密码
                  </Col>
              </div>
              <IceContainer>
                  <Step current={this.state.step} type="dot">
                      <Step.Item key={0} title="输入账号" />
                      <Step.Item key={1} title="重设密码" />
                  </Step>
              </IceContainer>
              {this.renderStep(this.state.step)}
          </div>
      );
  }
}



const styles = {
  form: {
    paddingLeft: '140px',
    paddingTop:'20px'
  },
  formLabel: {
    lineHeight: '40px',
    paddingRight: '30px',
  },
  mapLabel: {
    textAlign: 'right',
    lineHeight: '1.7rem',
  },
  formRow: {
    marginBottom: '20px',
    clear: 'both',
    overflow: 'hidden'
  },
  formRow1: {
    marginBottom: '20px',
  },
  formErrorWrapper: {
    marginTop: '5px',
  },
  simpleFluencyForm: {},
  IMGGG: {
    backgroundcolor:'#f1f1f1',
    color: '#0a7ac3',
    border: '1px solid #0d599a',
    marginTop: '20px'
  },
  formBorder: {
    backgroundcolor:'#f1f1f1',
    color: '#0a7ac3',
    border: '1px solid #0d599a',
    width:'100%',
    height:'auto',
    overflow: 'hidden'
  },
  head:{
    lineHeight: '70px',
    backgroundColor: '#0091ea',

  },
  headText:{
    fontWeight: 'bolder',
    paddingLeft:'240px',
    fontSize: '30px',
    color: 'yellow',
    lineHeight: '88px',
  },
  fontDiv:{
    height: '128px',
    width: '130px',
    textAlign: 'center',
    color: '#999',
    background: '#f5f5f5',
    lineHeight: '128px',
    textAlign: 'center',
    overflow: 'hidden',
  },
  imgMagg:{
    margin: '9px auto',
  },
  fontMagg:{
    margin: '40px auto',
  },
  shiImgMagg:{
    margin: '7px auto',
    width: '100%'
  },
  submitBtn: {
    fontSize: '13px',
    height: '40px',
    lineHeight: '40px',
    background: '#3080fe',
    borderRadius: '2px',
  },
  btns1: {
    margin: '25px 0',
    clear: 'both',
    overflow: 'hidden',
    width : '850px',
   
  },
  btns2: {
    margin: '25px 0',
    clear: 'both',
    overflow: 'hidden',
    width : '980px',
    marginLeft: '-94px',
  },
  scrollContainer: {
    width: '100%',
    height: '448px',
    overflow: 'auto',
  },
  formItemCol: {
    position: 'relative',
  },
};
