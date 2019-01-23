import React, { Component } from 'react';
export default class MonitorDetail extends Component {
  static displayName = 'MonitorDetail';

  static propTypes = {};

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    return (
      <div>
        <iframe style={{border:0,width:"100%",height:'800px'}} src="http://192.168.0.232/3D/3droom.html"/>
      </div>
    );
  }
}
