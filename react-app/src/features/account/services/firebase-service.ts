import firebase from "firebase/compat/app";
import { firebaseApp } from "../../../services/firebase-client";
import "firebase/compat/auth";

export const firebaseAuth = firebaseApp.auth();
export default firebaseApp;
export type FirebaseUser = firebase.User;
