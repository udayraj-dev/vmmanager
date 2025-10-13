<script>
  import Router from 'svelte-spa-router';
  import routes from './routes';
</script>

<h1>Virtual Machine Manager</h1>

<nav>
  <a href="#/">Home</a>
  <a href="#/add">Add VM</a>
</nav>

<Router {routes} />