import React from "react";
import "./App.css";

function App() {
  const showMessage = () => {
    alert("Welcome to React!");
  };

  return (
    <div className="container">
      <h1 className="title">Hello React!</h1>

      <p className="info">
        Name: <b>Your Name</b> <br />
        Class: <b>Your Class</b>
      </p>

      <button className="btn" onClick={showMessage}>
        Click Me
      </button>
    </div>
  );
}

export default App;
