import React, {Component} from "react";
import "./Dashboard.css";
import axios from "axios";

class Dashboard extends Component {
  constructor(props) {
    super(props);
    this.state = {
      date: new Date(),
      data: {
        comment: "xxx"
      },
      lastFetchSuccessful: true
    };
  }

  componentDidMount() {
    this.timerID = setInterval(
      () => this.loadData(),
      5000
    );
  }

  componentWillUnmount() {
    clearInterval(this.timerID);
  }

  loadData() {
    axios
      .get("http://localhost:8080/dashboard")
      .then(res => {
        this.state.data = res;
        this.state.date = new Date();
        this.state.lastFetchSuccessful = true;
      })
      .catch(error => {
        if (this.state.lastFetchSuccessful) {
          console.log(error);
          this.state.lastFetchSuccessful = false;
        }
      });
  }

  render() {
    return (
      <div>
        <div>
          {this.state.date.toLocaleTimeString()}
        </div>
        <div>
          {JSON.stringify(this.state.data)}
        </div>
        <table className="Dashboard">
          <tbody>
          <tr>
            <td>
              ADMkokhom
            </td>
          </tr>
          <tr>
            <td>
              ADMsomeone
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    );
  }
}

export default Dashboard;
