module.exports = {
    pwa: {
        name: 'SMB_Kirana',
        themeColor: '#4DBA87',
        msTileColor: '#000000',
      workboxPluginMode: 'InjectManifest',
      workboxOptions: {
        swSrc: 'service-worker.js',
      },
    },
  }