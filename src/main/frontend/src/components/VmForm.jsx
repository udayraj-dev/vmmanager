import React from 'react';

function VmForm({ vm, setVm, saveVM, testConnection, onCancel, loading, error }) {

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        setVm(prevVm => ({
            ...prevVm,
            [name]: type === 'checkbox' ? checked : value
        }));
    };

    const handleFormSubmit = (e) => {
        e.preventDefault();
        saveVM();
    };

    return (
        <div className="container mx-auto p-8">
            <h2 className="text-2xl font-bold mb-6">Add VM</h2>

            {error && (
                <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4" role="alert">
                    <strong className="font-bold">Error:</strong>
                    <span className="block sm:inline"> {error}</span>
                </div>
            )}

            <form
                onSubmit={handleFormSubmit} className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div className="md:col-span-2">
                    <label htmlFor="category" className="block text-sm font-medium text-gray-700">Category</label>
                    <input type="text" id="category" name="category" value={vm.category || ''} onChange={handleChange} className="p-2 mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50" placeholder="e.g., Training Servers" required />
                </div>

                <div className="md:col-span-2">
                    <label htmlFor="purpose" className="block text-sm font-medium text-gray-700">Purpose</label>
                    <textarea id="purpose" name="purpose" value={vm.purpose || ''} onChange={handleChange} rows="3" className="p-2 mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50" placeholder="e.g., Hosting the training server for laterals"></textarea>
                </div>

                <div>
                    <label htmlFor="ownerName" className="block text-sm font-medium text-gray-700">Owner Name</label>
                    <input type="text" id="ownerName" name="ownerName" value={vm.ownerName || ''} onChange={handleChange} className="p-2 mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50" placeholder="e.g., John Doe" required />
                </div>

                <div>
                    <label htmlFor="ipAddress" className="block text-sm font-medium text-gray-700">IP Address</label>
                    <input type="text" id="ipAddress" name="ipAddress" value={vm.ipAddress || ''} onChange={handleChange} className="p-2 mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50" placeholder="e.g., 192.168.1.10" required />
                </div>

                <div className="md:col-span-2 flex items-center">
                    <input id="isLinux" name="isLinux" type="checkbox" checked={vm.isLinux || false} onChange={handleChange} className="rounded border-gray-300 text-indigo-600 shadow-sm focus:border-indigo-300 focus:ring focus:ring-offset-0 focus:ring-indigo-200 focus:ring-opacity-50" />
                    <label htmlFor="isLinux" className="ml-2 block text-sm text-gray-900">Is Linux?</label>
                </div>

                <div>
                    <label htmlFor="username" className="block text-sm font-medium text-gray-700">Username</label>
                    <input type="text" id="username" name="username" value={vm.username || ''} onChange={handleChange} className="p-2 mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50" required />
                </div>

                <div>
                    <label htmlFor="password" className="block text-sm font-medium text-gray-700">Password</label>
                    <input type="password" id="password" name="password" value={vm.password || ''} onChange={handleChange} className="p-2 mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50" required />
                </div>

                {vm.isLinux && (
                    <div>
                        <div>
                            <label htmlFor="portNumber" className="block text-sm font-medium text-gray-700">Port Number</label>
                            <input type="number" id="portNumber" name="portNumber" value={vm.portNumber || ''} onChange={handleChange} className="p-2 mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50" />
                        </div>
                        <div>
                            <button type="button" onClick={testConnection} disabled={loading} className="mt-6 inline-flex justify-center py-2 px-4 rounded-md text-white bg-brand-orange hover:bg-brand-orange-lt cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed">
                                {loading ? 'Testing...' : 'Test Connection'}
                            </button>
                        </div>
                    </div>
                )}

                <hr className="md:col-span-2" />

                <div
                    className="md:col-span-2 bg-orange-100 border-l-4 border-orange-500 text-brand-orange p-4" role="alert">
                    <p className="font-bold">Heads up!</p>
                    <p>The following fields are auto-discovered for Linux VMs with Test connection. For Windows, please fill them in manually.</p>
                </div>

                <div>
                    <label htmlFor="hostName" className="block text-sm font-medium text-gray-700">Hostname</label>
                    <input type="text" id="hostName" name="hostName" value={vm.hostName || ''} onChange={handleChange} className="p-2 mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50" placeholder="e.g., web-server-01" required readOnly={vm.isLinux} />
                </div>

                <div>
                    <label htmlFor="osName" className="block text-sm font-medium text-gray-700">OS Name</label>
                    <input type="text" id="osName" name="osName" value={vm.osName || ''} onChange={handleChange} className="p-2 mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50" placeholder="e.g., Windows Server 2022" required readOnly={vm.isLinux} />
                </div>

                <div>
                    <label htmlFor="cpuCores" className="block text-sm font-medium text-gray-700">CPU Cores</label>
                    <input type="text" id="cpuCores" name="cpuCores" value={vm.cpuCores || ''} onChange={handleChange} className="p-2 mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50" placeholder="e.g., 4" required readOnly={vm.isLinux} />
                </div>

                <div>
                    <label htmlFor="totalRamSize" className="block text-sm font-medium text-gray-700">RAM Size</label>
                    <input type="text" id="totalRamSize" name="totalRamSize" value={vm.totalRamSize || ''} onChange={handleChange} className="p-2 mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50" placeholder="e.g., 8GB" required readOnly={vm.isLinux} />
                </div>

                <div>
                    <label htmlFor="rootPartitionSize" className="block text-sm font-medium text-gray-700">Root Partition Size</label>
                    <input type="text" id="rootPartitionSize" name="rootPartitionSize" value={vm.rootPartitionSize || ''} onChange={handleChange} className="p-2 mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50" placeholder="e.g., 100GB" required readOnly={vm.isLinux} />
                </div>

                <div>
                    <label htmlFor="macAddress" className="block text-sm font-medium text-gray-700">MAC Address</label>
                    <input type="text" id="macAddress" name="macAddress" value={vm.macAddress || ''} onChange={handleChange} className="p-2 mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50" placeholder="e.g., 00:1A:2B:3C:4D:5E" required readOnly={vm.isLinux} />
                </div>

                <div className="md:col-span-2 flex justify-end gap-4">
                    <button type="button" onClick={onCancel} className="inline-flex justify-center py-2 px-4 border border-gray-300 shadow-sm text-sm font-medium rounded-md text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500">
                        Cancel
                    </button>
                    <button type="submit" disabled={loading} className="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-brand-orange hover:bg-brand-orange-lt focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50">
                        {loading ? 'Saving...' : 'Save'}
                    </button>
                </div>
            </form>
        </div>
    );
}

export default VmForm;