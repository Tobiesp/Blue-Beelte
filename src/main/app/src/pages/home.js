import React from 'react';
import { getLastEventSnapshot, getRunningTotals } from './../services/StudentService'

class Home extends React.Component {

  token = ""

  constructor(props) {
    super(props);
    this.token = props.token;
  }

  render() {
    const eventData = getLastEventSnapshot(this.token);
    const studentData = getRunningTotals(this.token);
    return (
      <div 
        style={{
          display: 'flex',
          justifyContent: 'Right',
          alignItems: 'Right',
          height: '100vh'
        }}
      >
        <div>
            <h3>Last Event Count</h3>
              <table>
              <tr>
                <th>Group</th>
                <th>Count</th>
              </tr>
              {eventData.groups.map((val, key) => {
                return (
                  <tr key={key}>
                    <td>{val.group}</td>
                    <td>{val.count}</td>
                  </tr>
                );
              })}
            </table>
            <a>Total Students: {eventData.total}</a>
        </div>
        <div>
        <h1>Student Points</h1>
        <table>
            <tr>
              <th>Grade</th>
              <th>Student</th>
              <th>Group</th>
              <th>Points</th>
            </tr>
            {studentData.runningTotalsList.map((val, key) => {
                            return (
                              <tr key={key}>
                                <td>{val.student.grade}</td>
                                <td>{val.student.name}</td>
                                <td>{val.student.group.name}</td>
                                <td>{val.total}</td>
                              </tr>
                            );
                          })}
        </table>
        </div>
      </div>
    );
  }
}

export default Home;