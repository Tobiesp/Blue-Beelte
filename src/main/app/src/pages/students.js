import React from 'react';

class Students extends React.Component {

    constructor(props) {
        super(props);
        this.token = props.token
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