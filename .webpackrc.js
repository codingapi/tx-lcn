const webpack = require('webpack')

module.exports = {
	plugins:[
		new webpack.DefinePlugin({
			"host":process.env.ENV != 'dev' ?
				JSON.stringify("/admin/") :
				JSON.stringify("/admin/"),
		})
	]
}