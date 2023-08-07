import { Auth } from '@firebase/auth';
import React from 'react';
import { initializeApp } from 'firebase/app';
import AdminFile from '../admin.json';
import { getAuth } from 'firebase/auth';

function useAuth(): Auth {
  const [auth] = React.useState(() => {
    const app = initializeApp(AdminFile);
    return getAuth(app);
  });

  return auth;
}

export default useAuth;
