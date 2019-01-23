import React, { Component } from 'react';
import TrackTable from './components/TrackTable';
import {theRequest, isNull} from '../../common';
export default class Dashboard extends Component {
  static displayName = 'Dashboard';

  static propTypes = {};

  static defaultProps = {};

  constructor(props) {
    let groupId = "", unitId = "";
    if (!isNull(theRequest(props.location.search))) {
      groupId = theRequest(props.location.search).groupId;
      unitId = theRequest(props.location.search).unitId;
    }
    super(props);
    this.state = {
      groupId: groupId,
      unitId: unitId
    };
  }

  render() {
    return (
      <div>
        <TrackTable groupId={this.state.groupId} unitId={this.state.unitId} path={this.props}/>
      </div>
    );
  }
}
