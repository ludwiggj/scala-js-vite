import { defineConfig } from "vite";
import scalaJSPlugin from "@scala-js/vite-plugin-scalajs";

export default defineConfig({
  plugins: [scalaJSPlugin()],
  server: {
    proxy: {
        "/earthquake-api": {
          target: "https://earthquake.usgs.gov/fdsnws/event/1/query",
          changeOrigin: true,
          secure: false,   
          ws: true,
          rewrite: path => path.replace(/^\/earthquake-api/, ""),
          configure: (proxy, _options) => {
            proxy.on("error", (err, _req, _res) => {
              console.log("proxy error", err);
            });
            proxy.on('proxyReq', (proxyReq, req, _res) => {
              console.log("Sending Request to the Target:", req.method, req.url);
            });
            proxy.on('proxyRes', (proxyRes, req, _res) => {
              console.log("Received Response from the Target:", proxyRes.statusCode, req.url);
            });
          },
        }
      }
  }
});