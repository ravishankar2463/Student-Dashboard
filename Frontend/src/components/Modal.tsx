import React, { useState } from "react";
import Backdrop from "./Backdrop";

type Props = {
  className?: string;
  children?: React.ReactNode;
  closeModal: () => void;
};

function Modal({ className, children, closeModal }: Props) {
  function calculateTopPosition(): number {
    return scrollY + window.innerHeight * 0.5;
  }

  return (
    <>
      <Backdrop isOpen={true} closeBackdrop={closeModal}></Backdrop>
      <div
        style={{
          position: "absolute",
          top: calculateTopPosition(),
          right: "50%",
          transform: `translate(50%,-50%)`,
        }}
        className={`bg-white p-5 rounded-md z-50 ${
          className ? className : " "
        }`}
      >
        {children}
      </div>
    </>
  );
}

export default Modal;
