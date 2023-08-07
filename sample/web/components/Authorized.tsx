/* eslint-disable no-console */
import React from 'react';
import FirebaseUtils from '../lib/FirebaseUtils';
import { User } from '@firebase/auth';

const Authorized: React.FC<{ utils: FirebaseUtils; user: User }> = ({
  utils,
  user,
}) => {
  async function logToken() {
    console.log({ token: await utils.getToken() });
  }

  async function getJoke() {
    const token = await user.getIdToken();
    const headers = new Headers();
    headers.set('Authorization', 'Bearer ' + token);
    headers.set('Content-Type', 'application/json');
    const result = await fetch('http://localhost:8080/joke', {
      headers,
    }).then(it => it.text());
    alert(result);
  }

  return (
    <div className="mt-4">
      <div className="mx-auto my-4 flex w-full justify-center">
        <button className="btn-primary mx-2 bg-amber-400" onClick={getJoke}>
          Request a joke
        </button>
      </div>

      <button className="btn-primary mx-2" onClick={logToken}>
        Log token
      </button>
      <button className="btn-primary mx-2" onClick={utils.refreshToken}>
        Refresh token
      </button>
      <button className="btn-primary mx-2" onClick={utils.signOut}>
        Sign out
      </button>
    </div>
  );
};

export default Authorized;
