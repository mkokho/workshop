import React, {Component} from "react";
import "./Dashboard.css";
import axios from "axios";
import ReactTable from "react-table";
import "react-table/react-table.css";

const data = [{
  account: 'ADMkokhom',
  age: 26,
  friend: {
    name: 'Jason Maurer',
    age: 23,
  }
}];

const columns = [
  {
    Header: 'Name',
    accessor: 'name'
  },
  {
    Header: 'Account',
    accessor: 'account'
  },
  {
    Header: "ECS Cluster",
    columns: [
      {
        Header: "x",
        accessor: d => d.cluster.exists
      }
    ]
  }
];


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

  dataForAccount(account) {
    return {
      name: "Name " + account,
      account: account,
      cluster: {
        exists: false
      },
      hasService: false
    }
  }

  render() {
    var data = [
      this.dataForAccount("ADMkokhom"),
      this.dataForAccount("ADMjohns"),
    ];
    return (
      <div>
        <ReactTable
          showPagination={false}
          showPageSizeOptions={false}
          defaultPageSize={5}
          data={data}
          columns={columns}
        />
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
    )
      ;
  }
}

export default Dashboard;
