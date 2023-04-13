import React from 'react';

class Admin extends React.Component {

    constructor(props) {
      super(props);
      this.state = {show: false};
      window.AdminComponent = this;
    }
  
    showComponent() {
      this.setState({show: true});
    }
  
    hideComponent() {
      this.setState({show: false});
    }

    render() {
        return (
            <div id="admin_component"
                style={{
                    display: 'flex',
                    justifyContent: 'Right',
                    alignItems: 'Right',
                    height: '100vh'
                }}
            >
                <h1>Admin</h1>
            </div>
        );
    }
}

export default Admin;