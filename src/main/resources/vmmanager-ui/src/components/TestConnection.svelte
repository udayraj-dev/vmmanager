<script>
    let connection = {
      ipAddress: '',
      portNumber: 22,
      username: '',
      password: ''
    };
    let result = '';
  
    async function testConnection() {
      const res = await fetch('/vms/test-connection', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(connection)
      });
  
      result = await res.text();
    }
  </script>
  
  <h2>Test SSH Connection</h2>
  <form on:submit|preventDefault={testConnection}>
    <input bind:value={connection.ipAddress} placeholder="IP Address" />
    <input type="number" bind:value={connection.portNumber} placeholder="Port" />
    <input bind:value={connection.username} placeholder="Username" />
    <input type="password" bind:value={connection.password} placeholder="Password" />
    <button type="submit">Test</button>
  </form>
  
  <p>{result}</p>