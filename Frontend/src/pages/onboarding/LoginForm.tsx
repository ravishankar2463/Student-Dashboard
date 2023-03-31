import { AxiosError } from "axios";
import { FormEvent, useContext, useRef } from "react";
import { QueryClient } from "react-query";
import { ActionFunctionArgs, useActionData, useSubmit } from "react-router-dom";
import { login as studentDashboardApiLogin } from "../../api/studentDashboardApi";
import Button from "../../components/Button";
import FormInput from "../../components/FormInput";
import FormLabel from "../../components/FormLabel";
import { Login } from "../../models/Login";
import { Student } from "../../models/Student";
import { AuthContext } from "../../store/AuthContext";
import toast from "react-hot-toast";
import { redirect, useNavigation } from "react-router";
import Spinner from "../../components/Spinner";

type Props = {};

function validFormData(data: Login): boolean {
  return !Object.values(data).some(
    (v) => v === null || typeof v === "undefined" || v === ""
  );
}

function LoginForm({}: Props) {
  const authCtx = useContext(AuthContext);
  const email = useRef<HTMLInputElement>(null);
  const password = useRef<HTMLInputElement>(null);
  const student = useActionData() as Student;

  const submit = useSubmit();

  const navigation = useNavigation();
  const submitting = navigation.state === "submitting";

  if (student != null || student != undefined) {
    console.log(student);
    authCtx.login(student);

    if (authCtx.isLoggedIn) {
      redirect("/");
    }
  }

  const validateUser = (e: FormEvent) => {
    e.preventDefault();

    const data: Login = {
      email: email.current ? email.current.value : "",
      password: password.current ? password.current.value : "",
    };

    if (validFormData(data)) {
      submit(data, {
        method: "post",
        action: "/login/login-form",
      });
    } else {
      toast("Email Id and Password cannot be empty.");
    }
  };

  return (
    <form
      className="text-2xl flex flex-col items-center"
      onSubmit={validateUser}
    >
      <div className="pt-4 md:px-3">
        <FormLabel htmlFor="emailId" name="Email Id" />
        <FormInput
          id="emailId"
          type="email"
          placeholder="example@gmail.com"
          ref={email}
        />
      </div>

      <div className="pt-4 md:px-3">
        <FormLabel htmlFor="password" name="Password" />
        <FormInput
          id="password"
          type="password"
          placeholder="enter password"
          ref={password}
        />
      </div>

      <Button bgColor="#0f172a" textColor="white" type="submit">
        {submitting ? <Spinner /> : "Submit"}
      </Button>
    </form>
  );
}

export default LoginForm;

export const action =
  (queryClient: QueryClient) =>
  async ({ params, request }: ActionFunctionArgs) => {
    const formData = Object.fromEntries(await request.formData()) as Login;
    let error = null;
    let data = null;

    try {
      data = (await queryClient.fetchQuery("loggedinUser", () =>
        studentDashboardApiLogin(formData)
      )) as Student;
    } catch (err) {
      if (err instanceof AxiosError) {
        if (err.response?.status == 400) {
          error = "Please provide valid Email and Password.";
        } else {
          error =
            "There is some error with the server, Please check with the server Admin.";
        }
      }
    }

    if (error !== null) {
      toast(error);
    }

    return data;
  };
