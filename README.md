# ABC Contentful Integration

## Install
```
node v8.6+
run npm install
java
clone git repo
maven clean package -U
```

## Installing the Serverless Framework
Next, install the Serverless Framework via npm which was already installed when you installed Node.js.

Open up a terminal and type npm install -g serverless to install Serverless.
```sh
npm install -g serverless
```

Once the installation process is done you can verify that Serverless is installed successfully by running the following command in your terminal:
```sh
serverless
```
To see which version of serverless you have installed run:
```sh
serverless --version
```

## Setting up AWS
To run serverless commands that interface with your AWS account, you will need to setup your AWS account credentials on your machine.
[Follow these instructions on setting up AWS credentials](https://serverless.com/framework/docs/providers/aws/guide/credentials/)

## Update Environment Variables
clone repository 
Update config.dev.json or config.prod.json depends on youe serverless deployment 
```
{
    "cma_access_token":"",
    "space_id":"",
    "environment_id":"master",
	"default_language":"en",
	"source_s3_bucket":""
}
```

## Deploy Lambda function
This is the main method for doing deployments with the Serverless Framework:
```sh
serverless deploy
```