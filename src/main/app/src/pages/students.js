import React from 'react';

class Students extends React.Component {

    constructor(props) {
        super(props);
        this.state = {show: false};
        window.StudentComponent = this;
      }
    
      showComponent() {
        this.setState({show: true});
      }
    
      hideComponent() {
        this.setState({show: false});
      }

    render() {
        return (
            <div id="students_component"
                style={{
                    display: 'flex',
                    justifyContent: 'Right',
                    alignItems: 'Right',
                    height: '100vh'
                }}
            >
                <h1>Students</h1>
                This is a test!! Why is it not showing????
            </div>
        );
    }
}

export default Students;