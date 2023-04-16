import React from 'react';

class Home extends React.Component {

  token = ""

  constructor(props) {
    super(props);
    this.token = props.token
  }

  render() {
    return (
      <div 
        style={{
          display: 'flex',
          justifyContent: 'Right',
          alignItems: 'Right',
          height: '100vh'
        }}
      >
        <h1>Home</h1>
      </div>
    );
  }
}

export default Home;