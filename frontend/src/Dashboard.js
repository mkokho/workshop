import React, {Component} from "react";
import "./Dashboard.css";
import axios from "axios";
import ReactTable from "react-table";
import "react-table/react-table.css";
import ReactTooltip from 'react-tooltip'
import Octicon from 'react-octicon'



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
    accessor: 'ecsCluster',
    width: 100,
    Cell: function(row){
      return <div>
          <Octicon name="check" data-tip="task 1"/>
          <Octicon name="primitive-dot" data-tip="task 2"/>
        </div>;
    }
  },
  {
    Header: 'Account',
    accessor: 'account'
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
      ecsCluster: 1,
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
        <ReactTooltip delayShow={500}/>
        <ReactTable
          showPagination={false}
          showPageSizeOptions={false}
          defaultPageSize={5}
          data={data}
          columns={columns}
          className={"-highlight"}
        />
      </div>
    )
      ;
  }
}

export default Dashboard;
