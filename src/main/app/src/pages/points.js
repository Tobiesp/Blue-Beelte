import React from 'react';

class Points extends React.Component {

    constructor(props) {
      super(props);
      this.token = props.token
    }

    render() {
        return (
            <div id="points_component"
                style={{
                    display: 'flex',
                    justifyContent: 'Right',
                    alignItems: 'Right',
                    height: '100vh'
                }}
            >
                <h1>Points</h1>
            </div>
        );
    }
}

export default Points;