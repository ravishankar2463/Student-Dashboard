import React, { FormEvent, useRef } from "react";
import { QueryClient } from "react-query";
import { ActionFunctionArgs, redirect, useNavigation } from "react-router";
import { NavLink, useSubmit } from "react-router-dom";
import Button from "../../components/Button";
import FormInput from "../../components/FormInput";
import FormLabel from "../../components/FormLabel";
import { NewSignUp } from "../../models/NewSignUp";
import toast from "react-hot-toast";
import Spinner from "../../components/Spinner";
import { signUpNewStudent } from "../../api/studentDashboardApi";
import { AxiosError } from "axios";

type Props = {};

function validFormData(data: NewSignUp): boolean {
  return !Object.values(data).some(
    (v) => v === null || typeof v === "undefined" || v === ""
  );
}

function SignUpForm({}: Props) {
  const submit = useSubmit();
  const email = useRef<HTMLInputElement>(null);
  const name = useRef<HTMLInputElement>(null);
  const dob = useRef<HTMLInputElement>(null);
  const password = useRef<HTMLInputElement>(null);

  const navigation = useNavigation();
  const submitting = navigation.state === "submitting";

  console.log("navigation", navigation);

  const signInUser = (e: FormEvent) => {
    e.preventDefault();

    const data: NewSignUp = {
      email: email.current ? email.current.value : "",
      name: name.current ? name.current.value : "",
      dateOfBirth: dob.current ? dob.current.value : "",
      password: password.current ? password.current.value : "",
    };

    if (validFormData(data)) {
      console.log("Form Data", data);

      submit(data, {
        method: "post",
        action: "/sign-up/signup-form",
      });
    } else {
      toast("Please provide correct input data.");
    }
  };

  return (
    <form className="text-2xl flex flex-col items-center" onSubmit={signInUser}>
      <div className="md:px-3">
        <FormLabel htmlFor="emailId" name="Email Id" />
        <FormInput
          id="emailId"
          type="email"
          placeholder="example@gmail.com"
          ref={email}
        />
      </div>
      <div className="pt-4 md:px-3">
        <FormLabel htmlFor="name" name="Name" />
        <FormInput id="name" type="text" placeholder="full name" ref={name} />
      </div>
      <div className="pt-4 md:px-3 self-start">
        <FormLabel htmlFor="dob" name="DateOfBirth" />
        <FormInput id="dob" type="date" ref={dob} />
      </div>
      <div className="pt-4 md:px-3">
        <FormLabel htmlFor="password" name="Password" />
        <FormInput
          id="password"
          type="password"
          placeholder="enter password"
          ref={password}
        />
        <div className="flex justify-center items-center">
          <Button
            className="self-center"
            bgColor="#0f172a"
            textColor="white"
            type="submit"
            disabled={submitting ? true : false}
          >
            {submitting ? <Spinner /> : "Submit"}
          </Button>
        </div>
        <div className="text-center">
          <NavLink to="/login">Go back to login page</NavLink>
        </div>
      </div>
    </form>
  );
}

export default SignUpForm;

export const action =
  (queryClient: QueryClient) =>
  async ({ params, request }: ActionFunctionArgs) => {
    const formData = Object.fromEntries(await request.formData()) as NewSignUp;

    let data;

    let error = null;

    try {
      data = await queryClient.fetchQuery("loggedinUser", () =>
        signUpNewStudent(formData)
      );
    } catch (err) {
      if (err instanceof AxiosError) {
        if (
          err.response !== undefined &&
          (err.response.data.message !== null ||
            err.response.data.message !== undefined)
        ) {
          error = err.response?.data.message;
        } else {
          error =
            "There is some error with the server, Please check with the server Admin.";
        }
      }
    }

    if (error !== null) {
      toast(error);
    } else {
      toast("New User Created!");
      return redirect("/login");
    }

    return null;
  };
