import Head from 'next/head';
import useAuth from '../lib/useAuth';
import useUser from '../lib/useUser';
import CurrentUser from '../components/CurrentUser';
import React from 'react';
import FirebaseUtils from '../lib/FirebaseUtils';
import Unauthorized from '../components/Unauthorized';
import Authorized from '../components/Authorized';

export default function Home() {
  const auth = useAuth();
  const user = useUser(auth);
  const [utils] = React.useState(() => new FirebaseUtils(auth));

  const isLoggedIn = user !== null;
  return (
    <div className="flex min-h-screen flex-1 flex-col items-center justify-center p-8">
      <Head>
        <title>Sample Firebase Auth</title>
      </Head>

      <CurrentUser user={user} />
      {isLoggedIn ? (
        <Authorized user={user} utils={utils} />
      ) : (
        <Unauthorized utils={utils} />
      )}
    </div>
  );
}
