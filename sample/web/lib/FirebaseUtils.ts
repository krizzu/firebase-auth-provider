import { Auth, getIdToken, UserCredential } from '@firebase/auth';
import { GoogleAuthProvider, signInWithPopup, signOut } from 'firebase/auth';

const provider = new GoogleAuthProvider();
class FirebaseUtils {
  constructor(private auth: Auth) {}

  logIn = async (): Promise<UserCredential> => {
    return await signInWithPopup(this.auth, provider);
  };

  signOut = async () => {
    await signOut(this.auth);
  };

  getToken = async (): Promise<string | null> => {
    const user = this.auth.currentUser;
    if (!user) {
      return null;
    }
    return await getIdToken(user, false);
  };

  refreshToken = async (): Promise<string | null> => {
    const user = this.auth.currentUser;
    if (!user) {
      return null;
    }
    return await getIdToken(user, true);
  };
}

export default FirebaseUtils;
