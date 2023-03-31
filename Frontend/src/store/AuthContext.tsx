import React, { useState, useEffect } from "react";
import { Student } from "../models/Student";

export type AuthContext = {
  isLoggedIn: boolean;
  logout: () => void;
  login: (student: Student) => void;
  loggedInStudent: Student | null;
};

export const AuthContext = React.createContext<AuthContext>({
  isLoggedIn: false,
  logout: () => {},
  login: (_student: Student) => {},
  loggedInStudent: null,
});

type Props = {
  children?: React.ReactNode;
};

const AuthContextProvider = ({ children }: Props) => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [loggedInStudent, setLoggedInStudent] = useState<Student | null>(null);

  useEffect(() => {
    const storedUserLoggedInInformation = sessionStorage.getItem("isLoggedIn");
    if (storedUserLoggedInInformation === "1") {
      setIsLoggedIn(true);
      const storedUser = JSON.parse(
        sessionStorage.getItem("loggedInStudent") || ""
      ) as Student;

      setLoggedInStudent(storedUser);
    }
  }, []);

  const logoutHandler = () => {
    setLoggedInStudent(null);
    setIsLoggedIn(false);
    sessionStorage.setItem("isLoggedIn", "0");
    sessionStorage.setItem("loggedInStudent", JSON.stringify(""));
  };

  const loginHandler = (student: Student) => {
    sessionStorage.setItem("isLoggedIn", "1");
    setLoggedInStudent(student);
    sessionStorage.setItem("loggedInStudent", JSON.stringify(student));
    setIsLoggedIn(true);
  };

  return (
    <AuthContext.Provider
      value={{
        isLoggedIn: isLoggedIn,
        logout: logoutHandler,
        login: loginHandler,
        loggedInStudent: loggedInStudent,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export default AuthContextProvider;
