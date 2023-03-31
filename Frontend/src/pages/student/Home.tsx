import React from "react";
import { Outlet } from "react-router";
import Navigation from "../../components/Navigation";

type Props = {};

function Home({}: Props) {
  return (
    <>
      <Navigation />
      <Outlet />
    </>
  );
}

export default Home;
