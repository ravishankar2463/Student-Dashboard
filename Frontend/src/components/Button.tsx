import React from "react";

type Props = {
  children: React.ReactNode;
  type: "button" | "submit" | "reset" | undefined;
  bgColor: string;
  textColor?: string;
  rounded?: boolean;
  className?: string;
  onClick?: () => void;
  disabled?: boolean;
};

function Button({
  children,
  type,
  bgColor,
  textColor,
  rounded,
  className,
  onClick,
  disabled,
}: Props) {
  return (
    <button
      style={{
        backgroundColor: bgColor,
        color: textColor ? textColor : "black",
      }}
      className={`py-2 px-5 my-4 ${rounded ? "rounded-md" : ""} ${
        className ? className : ""
      } ${disabled ? "cursor-not-allowed" : ""}`}
      type={type}
      onClick={onClick}
      disabled={disabled ? disabled : false}
    >
      {children}
    </button>
  );
}

export default Button;
