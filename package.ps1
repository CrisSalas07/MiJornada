<#
.SYNOPSIS
    Builds the delivery archive Apellido_Nombre_IF0009_AndroidNativo.zip in the project folder.

.DESCRIPTION
    Zips the project sources so they can be opened in Android Studio, leaving out generated and
    machine-specific files: build/, .gradle/, .idea/ (at any depth) and local.properties.

.EXAMPLE
    .\package.ps1 -Apellido Salas
#>
param(
    # AUTHOR: pass your surname with -Apellido, or replace this default.
    [string]$Apellido = "APELLIDO",
    [string]$Nombre = "Cristopher"
)

$ErrorActionPreference = "Stop"

$root = $PSScriptRoot
$projectFolder = Split-Path $root -Leaf
$zipName = "${Apellido}_${Nombre}_IF0009_AndroidNativo.zip"
$zipPath = Join-Path $root $zipName

$excludedDirs = @("build", ".gradle", ".idea")
$excludedFiles = @("local.properties")

$files = Get-ChildItem -Path $root -Recurse -File -Force | Where-Object {
    $relative = $_.FullName.Substring($root.Length + 1)
    $folder = Split-Path $relative -Parent
    $folderParts = if ($folder) { $folder.Split('\') } else { @() }
    $inExcludedDir = $folderParts | Where-Object { $excludedDirs -contains $_ }
    # Older delivery archives are skipped so a ZIP never ends up inside the next one.
    -not $inExcludedDir -and ($excludedFiles -notcontains $_.Name) -and ($_.Name -notlike "*_IF0009_AndroidNativo.zip")
}

Add-Type -AssemblyName System.IO.Compression, System.IO.Compression.FileSystem
if (Test-Path $zipPath) {
    Remove-Item $zipPath
}

# ZipFile is used instead of Compress-Archive because Compress-Archive flattens a list of files and loses the folders.
$zip = [System.IO.Compression.ZipFile]::Open($zipPath, [System.IO.Compression.ZipArchiveMode]::Create)
try {
    foreach ($file in $files) {
        $entryName = "$projectFolder/" + $file.FullName.Substring($root.Length + 1).Replace('\', '/')
        [System.IO.Compression.ZipFileExtensions]::CreateEntryFromFile($zip, $file.FullName, $entryName) | Out-Null
    }
}
finally {
    $zip.Dispose()
}

Write-Host "Created $zipPath with $($files.Count) files."
