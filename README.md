# The Cognitive System Command Line Interface - CSCLI
<br>

This is a command line interface (CLI) to easily create [CST projects](https://cst.fee.unicamp.br).
> [!WARNING]  
> **This project has been moved to CST-Group team. Please access [this page](https://github.com/CST-Group/CST-CLI) to see current project state.**



## Installation

### Install from source

1. Clone this repository into a local folder
    ```shell
   git clone https://github.com/brgsil/CST-CLI.git
   ```
2. Go into the repository directory and run the package task
   ```shell
   cd CST-CLI
   ./gradlew jpackage
   ```
3. Install deb package
   ```shell
   apt install ./build/cst_cli-package/cscli_0.1-1_amd64.deb
   ```
4. Update `PATH` variable to include executable
   ```shell
   echo 'export PATH="$PATH:/opt/cscli/bin"' >> ~/.bashrc
   source ~/.bashrc
   ```

## Example

To create a new example CST project run the command:
```shell
cst init -f test.yaml
```
