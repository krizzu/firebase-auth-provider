import { Auth, User } from '@firebase/auth';
import React from 'react';

function useUser(auth: Auth) {
  const [user, setUser] = React.useState<User | null>(() => auth.currentUser);

  React.useEffect(() => {
    return auth.onAuthStateChanged(newUser => {
      setUser(newUser);
    });
  }, [auth]);

  return user;
}

export default useUser;
