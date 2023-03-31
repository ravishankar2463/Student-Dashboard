import React, { KeyboardEvent, useEffect, useState } from "react";

type Props = {
  children?: React.ReactNode;
  isOpen: boolean;
  closeBackdrop: () => void;
};

// call this to Disable
function disableScroll() {
  document.body.style.overflow = "hidden";
}

// call this to Enable
function enableScroll() {
  document.body.style.overflow = "unset";
}

function Backdrop({ children, isOpen, closeBackdrop }: Props) {
  useEffect(() => {
    disableScroll();
  }, []);

  return (
    <div
      style={{ top: scrollY, left: 0, position: "absolute" }}
      onClick={() => {
        enableScroll();
        closeBackdrop();
      }}
      className={`z-40 bg-black/30 h-screen w-full ${isOpen ? "" : "hidden"}`}
    >
      <div className="flex h-full justify-center place-items-center">
        {children}
      </div>
    </div>
  );
}

export default Backdrop;
