import { firebaseAuth } from "./firebase-service";

export function firebaseLogin(email: string, password: string) {
  return firebaseAuth.signInWithEmailAndPassword(email, password);
}

export function firebaseLogout() {
  return firebaseAuth.signOut();
}

export function getCurrentUser() {
  return firebaseAuth.currentUser;
}

export function getAuthToken() {
  return firebaseAuth.currentUser?.getIdToken();
}
