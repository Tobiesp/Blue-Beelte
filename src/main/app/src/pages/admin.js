import React from 'react';

class Admin extends React.Component {

    constructor(props) {
      super(props);
      this.token = props.token
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