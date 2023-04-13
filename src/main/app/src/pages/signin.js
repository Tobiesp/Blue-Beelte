import React from 'react';

class SignIn extends React.Component {

    constructor(props) {
      super(props);
      this.state = {show: false};
      window.SignInComponent = this;
    }
  
    showComponent() {
      this.setState({show: true});
    }
  
    hideComponent() {
      this.setState({show: false});
    }

    render() {
        return (
            <div id="signin_component"
                style={{
                    display: 'flex',
                    justifyContent: 'Right',
                    alignItems: 'Right',
                    height: '100vh'
                }}
            >
                <h1>SignIn</h1>
            </div>
        );
    }
}

export default SignIn;