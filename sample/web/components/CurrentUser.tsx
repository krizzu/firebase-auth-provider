import React from 'react';
import { User } from '@firebase/auth';

const CurrentUser: React.FC<{ user: User | null }> = ({ user }) => {
  const isLoggedIn = user !== null;
  return (
    <div>
      {isLoggedIn ? <p>Logged in!</p> : <p>Logged out!</p>}
      {!isLoggedIn ? null : (
        <>
          <div>
            <p>
              Name: <span className="font-bold">{user.displayName}</span>
            </p>
            <p>
              Email: <span className="font-bold">{user.email}</span>
            </p>
          </div>
        </>
      )}
    </div>
  );
};

export default CurrentUser;
