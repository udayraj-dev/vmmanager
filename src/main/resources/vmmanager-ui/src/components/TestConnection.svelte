<script>
    let connection = {
      ipAddress: '',
      portNumber: 22,
      username: '',
      password: ''
    };
    let result = '';
    let loading = false;
  
    async function testConnection() {
      loading = true;
      result = '';
      try {
        const res = await fetch('/api/vms/test-connection', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(connection)
        });
    
        result = await res.text();
      } catch (e) {
        result = e.message;
      } finally {
        loading = false;
      }
    }
  </script>
  
  <div class="container mx-auto p-8">
    <h2 class="text-2xl font-bold mb-6">Test SSH Connection</h2>
    <form on:submit|preventDefault={testConnection} class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div>
            <label for="ipAddress" class="block text-sm font-medium text-gray-700">IP Address</label>
            <input id="ipAddress" bind:value={connection.ipAddress} class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50" placeholder="IP Address" />
        </div>
        <div>
            <label for="portNumber" class="block text-sm font-medium text-gray-700">Port</label>
            <input id="portNumber" type="number" bind:value={connection.portNumber} class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50" placeholder="Port" />
        </div>
        <div>
            <label for="username" class="block text-sm font-medium text-gray-700">Username</label>
            <input id="username" bind:value={connection.username} class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50" placeholder="Username" />
        </div>
        <div>
            <label for="password" class="block text-sm font-medium text-gray-700">Password</label>
            <input id="password" type="password" bind:value={connection.password} class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50" placeholder="Password" />
        </div>
        <div class="md:col-span-2 flex justify-end">
            <button type="submit" disabled={loading} class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50">
                {#if loading}Testing...{:else}Test{/if}
            </button>
        </div>
    </form>
  
    {#if result}
        <div class="mt-6 bg-gray-50 p-4 rounded-md">
            <h3 class="text-lg font-medium text-gray-900">Result</h3>
            <pre class="mt-2 text-sm text-gray-600">{result}</pre>
        </div>
    {/if}
  </div>