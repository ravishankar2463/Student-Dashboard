import { useContext, useEffect, useState } from "react";
import { Outlet, useNavigate } from "react-router-dom";
import "./App.css";
import { AuthContext } from "./store/AuthContext";

function App() {
  const authCtx = useContext(AuthContext);
  const navigate = useNavigate();

  useEffect(() => {
    if (authCtx.isLoggedIn) {
      navigate(`/home/${authCtx.loggedInStudent?.id}`);
    } else {
      navigate("/login");
    }
  }, [authCtx.isLoggedIn]);

  return <Outlet />;
}

export default App;
