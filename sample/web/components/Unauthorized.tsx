import React from 'react';
import FirebaseUtils from '../lib/FirebaseUtils';

const Unauthorized: React.FC<{ utils: FirebaseUtils }> = ({ utils }) => {
  return (
    <div>
      <button className="btn-primary mt-4" onClick={() => utils.logIn()}>
        Log in with Google
      </button>
    </div>
  );
};

export default Unauthorized;
