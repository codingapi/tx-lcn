import React, { Component } from 'react';
import TrackTable from './components/TrackTable';

export default class Dashboard extends Component {
  static displayName = 'Dashboard';

  static propTypes = {};

  static defaultProps = {};

  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    return (
      <div>
        <TrackTable path={this.props}/>
      </div>
    );
  }
}
