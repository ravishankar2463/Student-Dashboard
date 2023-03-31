import React, { useContext } from "react";
import { useNavigate } from "react-router";
import { AuthContext } from "../store/AuthContext";
import Button from "./Button";

type Props = {};

function Navigation({}: Props) {
  const authCtx = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    authCtx.logout();
  };

  return (
    <div className="min-h-[10vh] bg-zinc-800 text-white flex justify-between items-center">
      <div id="logo" className="ml-5">
        <h1 className="text-stone-400 uppercase">Student Dashboard</h1>
      </div>
      <div id="links" className="flex items-center gap-3 pr-5">
        <a
          className="uppercase cursor-pointer"
          onClick={() => navigate(`/home/${authCtx.loggedInStudent?.id}`)}
        >
          Home
        </a>
        <Button
          type="button"
          rounded
          bgColor="white"
          className="uppercase"
          onClick={handleLogout}
        >
          Logout
        </Button>
      </div>
    </div>
  );
}

export default Navigation;
