import React from 'react';

function Header() {
  return (
    <header>
      <div className="container mx-auto flex items-center justify-between px-4 py-6">
        <div className="flex items-center">
          {/* <img src="/assets/vmmanager.svg" alt="VMs Logo" className="mr-3 h-8 w-8" /> */}
          <h1 className="mr-10 text-4xl leading-tight font-thin whitespace-nowrap text-brand-orange">
            VMs
          </h1>
        </div>
        <div className="container mx-auto flex px-4 py-6">
          <input className="p-2 mr-4 flex-grow rounded-md border-brand-orange-lt" type="text" name="search" placeholder="Enter an IP or name of a VM to search for" />
          <button className="cursor-pointer rounded-md bg-brand-orange px-4 py-2 text-white hover:bg-brand-orange-lt" type="submit">Search</button>
        </div>
      </div>
    </header>
  );
}

export default Header;
