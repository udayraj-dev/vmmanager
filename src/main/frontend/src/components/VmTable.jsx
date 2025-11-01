import React from 'react';

function VmTable({ vms, loading, error }) {
  return (
    <table className="border-collapse p-4 lg:w-full rounded-md shadow-md bg-white">
      <thead className="border-brand-orange border-b-2">
        <tr>
          <th className="p-2 text-left text-lg">Host name</th>
          <th className="p-2 text-left text-lg">IP Address</th>
          <th className="p-2 text-left text-lg">MAC Address</th>
          <th className="p-2 text-left text-lg">Main Application</th>
          <th className="p-2 text-left text-lg">Purpose</th>
          <th className="p-2 text-left text-lg">Operating System</th>
        </tr>
      </thead>
      <tbody className="divide-y divide-brand-beige">
        {loading && (
          <tr><td colSpan="6" className="p-4 text-center text-gray-500">Loading...</td></tr>
        )}
        {error && (
          <tr><td colSpan="6" className="p-4 text-center text-red-500">Error: {error}</td></tr>
        )}
        {!loading && !error && vms.map((vm) => (
          <tr key={vm.id} className="hover:bg-brand-beige-lt cursor-pointer">
            <td className="p-2">{vm.hostName}</td>
            <td className="p-2">{vm.ipAddress}</td>
            <td className="p-2">{vm.macAddress}</td>
            <td className="p-2">{vm.category}</td>
            <td className="p-2">{vm.purpose}</td>
            <td className="p-2">{vm.osName}</td>
          </tr>
        ))}
        {!loading && !error && vms.length === 0 && (
          <tr><td colSpan="6" className="p-4 text-center text-gray-500">No virtual machines found.</td></tr>
        )}
      </tbody>
    </table>
  );
}

export default VmTable;
