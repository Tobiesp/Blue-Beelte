import React from 'react';

class Home extends React.Component {

  constructor(props) {
    super(props);
    this.state = {show: false};
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